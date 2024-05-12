package com.ssafy.fluffitmember.exercise.controller;

import com.ssafy.fluffitmember._common.error.ErrorResponse;
import com.ssafy.fluffitmember._common.error.ErrorType;
import com.ssafy.fluffitmember._common.exception.NotFoundUserException;
import com.ssafy.fluffitmember._common.success.SuccessResponse;
import com.ssafy.fluffitmember._common.success.SuccessType;
import com.ssafy.fluffitmember.exercise.dto.request.RunningReqDto;
import com.ssafy.fluffitmember.exercise.dto.request.StepsReqDto;
import com.ssafy.fluffitmember.exercise.dto.response.RunningResDto;
import com.ssafy.fluffitmember.exercise.dto.response.StepsResDto;
import com.ssafy.fluffitmember.exercise.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exercise")
public class ExerciseController {

    private final ExerciseService exerciseService;


    @PostMapping("/running")
    public ResponseEntity<Object> getRunningReword(@RequestHeader("memberId") String memberId, @RequestBody RunningReqDto runningReqDto){
        try {
            RunningResDto runningResDto = exerciseService.getRunningReword(memberId,runningReqDto);
            return ResponseEntity.ok().body(runningResDto);
        }catch (NotFoundUserException e){
            return ResponseEntity.badRequest().body(ErrorResponse.from(ErrorType.NOT_FOUND_MEMBER));
        }
    }

    @PostMapping("/steps")
    public ResponseEntity<Object> getStepsReword(@RequestHeader("memberId") String memberId, @RequestBody StepsReqDto stepsReqDto){
        try {
            StepsResDto stepsResDto= exerciseService.getStepsReword(memberId,stepsReqDto);
            return ResponseEntity.ok().body(stepsResDto);
        }catch (NotFoundUserException e){
            return ResponseEntity.badRequest().body(ErrorResponse.from(ErrorType.NOT_FOUND_MEMBER));
        }
    }

}
