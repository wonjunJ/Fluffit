package com.ssafy.fluffitflupet.scheduler

import com.ssafy.fluffitflupet.entity.MemberFlupet
import java.time.LocalDateTime

class DecreaseCalculator {
    suspend fun calAchaTime(data: MemberFlupet){
        var curHour = LocalDateTime.now().hour
        var hours = 0 //총 몇 시간 뒤에 0이 되는지
        var fullness = data.fullness
        var health = data.health

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

        data.achaTime = LocalDateTime.now().plusHours(hours - 1L)
    }
}