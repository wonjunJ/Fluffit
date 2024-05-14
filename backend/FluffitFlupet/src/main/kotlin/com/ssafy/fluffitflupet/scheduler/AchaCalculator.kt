package com.ssafy.fluffitflupet.scheduler

import com.ssafy.fluffitflupet.entity.MemberFlupet
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class AchaCalculator {
    suspend fun calAchaTime(full: Int, healthy: Int): LocalDateTime? {
        var curHour = LocalDateTime.now().hour
        var hours = 0 //총 몇 시간 뒤에 0이 되는지
        var fullness = full //코틀린에서는 매개변수로 전달된 Int와 같은 타입들(값 복사로 전달)을 변경하고 싶다면 var변수에 다시 할당
        var health = healthy

        while(fullness > 0 && health > 0){
            if(curHour in 0..7){ //포만감 2시간에 5씩, 건강 2시간에 3씩
                if(curHour + 2 <= 8){
                    fullness -= 5
                    health -= 3
                    curHour += 2
                    hours += 2
                }else{
                    val remainingHoursTo8 = 8 - curHour
                    val decreaseFullAmount = (5 * remainingHoursTo8) / 2
                    val decreaseHealthAmount = (3 * remainingHoursTo8) / 2
                    fullness -= decreaseFullAmount
                    health -= decreaseHealthAmount
                    hours += remainingHoursTo8
                    curHour = 8
                }
            }else{ //포만감 1시간에 5씩, 건강 1시간에 3씩
                fullness -= 5
                health -= 3
                curHour = (curHour + 1) % 24; // 24시간 주기
                hours += 1;
            }

            if(fullness <= 0 || health <= 0){
                break
            }
        }

        if(hours > 1){ //죽기까지 한시간밖에 안남았을 경우에는 새로 업데이트할 필요x
            return LocalDateTime.now().plusHours(hours - 1L)
        }else{
            return null
        }
    }
}