package com.ssafy.fluffitbattle.client;

import com.ssafy.fluffitbattle.entity.dto.FlupetInfoClientDto;
import com.ssafy.fluffitbattle.entity.dto.FlupetInfoTempClientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "flupet-service")
public interface FlupetFeignClient {
    @GetMapping("/flupet/info")
    FlupetInfoTempClientDto getFlupetTempInfo(@RequestHeader("memberId") String memberId);

    @GetMapping("/flupet/battle-info")
    FlupetInfoClientDto getFlupetInfo(@RequestHeader("memberId") String memberId);
}
