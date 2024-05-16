package com.ssafy.fluffitflupet.service

import com.ssafy.fluffitflupet.client.MemberServiceClientAsync
import com.ssafy.fluffitflupet.dto.CoinKafkaDto
import com.ssafy.fluffitflupet.dto.FeedingResponse
import com.ssafy.fluffitflupet.dto.FoodListResponse
import com.ssafy.fluffitflupet.entity.FoodType
import com.ssafy.fluffitflupet.error.ErrorType
import com.ssafy.fluffitflupet.exception.CustomBadRequestException
import com.ssafy.fluffitflupet.messagequeue.KafkaProducer
import com.ssafy.fluffitflupet.repository.FoodRepository
import com.ssafy.fluffitflupet.repository.MemberFlupetRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.redisson.api.RLockReactive
import org.redisson.api.RedissonClient
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class FoodService(
    private val foodRepository: FoodRepository,
    private val client: MemberServiceClientAsync,
    private val memberFlupetRepository: MemberFlupetRepository,
    private val reactiveRedisTemplate: ReactiveRedisTemplate<String, String>,
    private val redissonClient: RedissonClient,
    private val kafkaProducer: KafkaProducer
) {
    //lombok slf4j를 쓰기 위해
    private val log = LoggerFactory.getLogger(FlupetService::class.java)

    suspend fun getFeedList(userId: String): FoodListResponse = coroutineScope {
        val foods = ArrayList<FoodListResponse.Food>()
        val foodlist = withContext(Dispatchers.IO){ foodRepository.findAll() }
        val coinWait = async { client.getUserCoin(userId) }
        foodlist.collect{ value ->
            foods.add(FoodListResponse.Food(
                id = value.id,
                name = value.name,
                fullnessEffect = value.fullnessEffect,
                healthEffect = value.healthEffect,
                price = value.price,
                imageUrl = value.imgUrl,
                description = value.description
            ))
        }
        return@coroutineScope FoodListResponse(
            foods = foods,
            coin = coinWait.await().coin
        )
    }

    suspend fun feedToPet(userId: String, foodId: Long): FeedingResponse = coroutineScope {
        val foodWait = async(Dispatchers.IO) { foodRepository.findById(foodId) }
        val mflupet = async(Dispatchers.IO) { memberFlupetRepository.findByMemberIdAndIsDeadIsFalse(userId).awaitSingleOrNull() }
        val coinWait = async { client.getUserCoin(userId) }
        var coin = 0
        val food: FoodType

        val lockName = "Member-$userId"
        val rLock: RLockReactive = redissonClient.reactive().getLock(lockName)
        val waitTime = 6L //락 획득을 위해 기다리는 시간
        val leaseTime = 4L //락을 최대 임대하는 시간

        var lockAcquired = false
        withContext(Dispatchers.Default){
            try {
                val available = rLock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS).awaitSingle()
                if(!available){
                    coinWait.cancel()
                    throw CustomBadRequestException(ErrorType.LOCK_NOT_AVAILABLE)
                }
                lockAcquired = true
                //=== 락 획득 후 로직 수행 ===
                log.info("락 획득 로직 수행 시작")
                food = foodWait.await() ?: throw CustomBadRequestException(ErrorType.INVALID_FOODID)
                if(food.stock <= 0){
                    throw CustomBadRequestException(ErrorType.INSUFFICIENT_FOOD_STOCK)
                }
                val mflupetRst = mflupet.await() ?: throw CustomBadRequestException(ErrorType.INVALID_USERID)
                if(mflupetRst.fullness == 100){
                    throw CustomBadRequestException(ErrorType.NOT_REQUIRED_FULLNESS)
                }
                coin = coinWait.await().coin
                if(coin < food.price){
                    throw CustomBadRequestException(ErrorType.INSUFFICIENT_COIN)
                }
                //여기에서 카프카로 코인 데이터 동기화 요청
                log.info("카프카 요청 시작")
                kafkaProducer.send("coin-update", CoinKafkaDto(memberId = userId, price = food.price))

                mflupetRst.fullness = if(mflupetRst.fullness + food.fullnessEffect >= 100) 100
                else mflupetRst.fullness + food.fullnessEffect
                mflupetRst.health = if(mflupetRst.health + food.healthEffect >= 100) 100
                else mflupetRst.health + food.healthEffect
                withContext(Dispatchers.IO) {
                    memberFlupetRepository.save(mflupetRst).awaitSingle()
                    food.stock--
                    foodRepository.save(food)
                }
//            food.stock--
//            withContext(Dispatchers.IO){ foodRepository.save(food) }
            }catch (e: InterruptedException){
                //락을 얻으려고 시도하다가 인터럽트를 받았을 때 발생하는 예외
                coinWait.cancel()
                log.error(e.message)
                throw CustomBadRequestException(ErrorType.LOCK_INTERRUPTED_ERROR)
            }finally {
                if(lockAcquired){
                    rLock.unlock().awaitSingleOrNull()
                }
            }
        }
//        try {
//            withContext(Dispatchers.IO){
//                val available = rLock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS).awaitSingle()
//                if(!available){
//                    coinWait.cancel()
//                    throw CustomBadRequestException(ErrorType.LOCK_NOT_AVAILABLE)
//                }
//                lockAcquired = true
//            }
//            //=== 락 획득 후 로직 수행 ===
//            log.info("락 획득 로직 수행 시작")
//            food = foodWait.await() ?: throw CustomBadRequestException(ErrorType.INVALID_FOODID)
//            if(food.stock <= 0){
//                throw CustomBadRequestException(ErrorType.INSUFFICIENT_FOOD_STOCK)
//            }
//            val mflupetRst = mflupet.await() ?: throw CustomBadRequestException(ErrorType.INVALID_USERID)
//            if(mflupetRst.fullness == 100){
//                throw CustomBadRequestException(ErrorType.NOT_REQUIRED_FULLNESS)
//            }
//            coin = coinWait.await().coin
//            if(coin < food.price){
//                throw CustomBadRequestException(ErrorType.INSUFFICIENT_COIN)
//            }
//            //여기에서 카프카로 코인 데이터 동기화 요청
//            log.info("카프카 요청 시작")
//            kafkaProducer.send("coin-update", CoinKafkaDto(memberId = userId, price = food.price))
//
//            mflupetRst.fullness = if(mflupetRst.fullness + food.fullnessEffect >= 100) 100
//                                                        else mflupetRst.fullness + food.fullnessEffect
//            mflupetRst.health = if(mflupetRst.health + food.healthEffect >= 100) 100
//                                                        else mflupetRst.health + food.healthEffect
//            withContext(Dispatchers.IO) {
//                memberFlupetRepository.save(mflupetRst).awaitSingle()
//                food.stock--
//                foodRepository.save(food)
//            }
////            food.stock--
////            withContext(Dispatchers.IO){ foodRepository.save(food) }
//        }catch (e: InterruptedException){
//            //락을 얻으려고 시도하다가 인터럽트를 받았을 때 발생하는 예외
//            coinWait.cancel()
//            log.error(e.message)
//            throw CustomBadRequestException(ErrorType.LOCK_INTERRUPTED_ERROR)
//        }finally {
//            if(lockAcquired){
//                withContext(Dispatchers.IO) {
//                    rLock.unlock().awaitSingleOrNull()
//                }
//            }
//        }
        return@coroutineScope FeedingResponse(
            totalCoin = coin - food.price,
            fullnessEffect = food.fullnessEffect,
            healthEffect = food.healthEffect
        )
    }
}