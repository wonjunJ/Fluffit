package com.ssafy.fluffitmember._common.client;


import com.ssafy.fluffitmember._common.client.dto.RankFlupetInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "flupet-service")
public interface FlupetClient {

    @GetMapping("/flupet/rank-info")
    List<RankFlupetInfoDto> getFlupetRanks(@RequestParam("memberIds") List<String> memberIds);
}
