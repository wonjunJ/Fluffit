package com.ssafy.fluffitbattle.client;

import com.ssafy.fluffitbattle.entity.dto.GetPointResDto;
import com.ssafy.fluffitbattle.entity.dto.NickNameClientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "member-service")
public interface MemberFeignClient {
    @GetMapping("/member/nickname")
    NickNameClientDto getNickName(@RequestHeader("memberId") String memberId);

    @GetMapping("/member/get-point")
    GetPointResDto getBattlePoint(@RequestHeader("memberId") String memberId);
}


