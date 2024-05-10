package com.ssafy.fluffitmember.member.controller;

import com.ssafy.fluffitmember.error.ErrorResponse;
import com.ssafy.fluffitmember.error.ErrorStateCode;
import com.ssafy.fluffitmember.exception.DuplicateNickname;
import com.ssafy.fluffitmember.exception.NotFoundUserException;
import com.ssafy.fluffitmember.exception.NotValidNickname;
import com.ssafy.fluffitmember.member.dto.UpdateNicknameReqDto;
import com.ssafy.fluffitmember.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PutMapping("/update-nickname")
    public ResponseEntity<Object> updateNickname(@RequestHeader("memberId") String memberId, @RequestBody UpdateNicknameReqDto updateNicknameReqDto){
        try {
            log.info("memberId" + memberId);
            memberService.updateNickname(memberId,updateNicknameReqDto);
            return ResponseEntity.ok().build();
        } catch (NotFoundUserException e){
            return ResponseEntity.badRequest().body(ErrorResponse.from(ErrorStateCode.NOT_FOUND_MEMBER));
        } catch (NotValidNickname e) {
            return ResponseEntity.badRequest().body(ErrorResponse.from(ErrorStateCode.NOT_VALID_NICKNAME));
        } catch (DuplicateNickname e) {
            return ResponseEntity.badRequest().body(ErrorResponse.from(ErrorStateCode.DUPLICATE_NICKNAME));
        }
    }

    @GetMapping("/get-coin")
    public int getUserCoin(@RequestHeader("memberId") String memberId){
        return memberService.getUserCoin(memberId);
    }

}