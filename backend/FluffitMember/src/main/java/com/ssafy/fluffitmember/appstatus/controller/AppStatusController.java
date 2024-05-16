package com.ssafy.fluffitmember.appstatus.controller;

import com.ssafy.fluffitmember.appstatus.dto.request.NewVersionReqDto;
import com.ssafy.fluffitmember.appstatus.dto.request.UpdateVersionReqDto;
import com.ssafy.fluffitmember.appstatus.dto.response.NewVersionResDto;
import com.ssafy.fluffitmember.appstatus.dto.response.UpdateVersionResDto;
import com.ssafy.fluffitmember.appstatus.dto.response.VersionCheckResDto;
import com.ssafy.fluffitmember.appstatus.service.AppStatusService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app-status")
@RequiredArgsConstructor
public class AppStatusController {

    private final AppStatusService appStatusService;

    @GetMapping("/version-check")
    public ResponseEntity<Object> versionCheck(){
        VersionCheckResDto versionCheckResDto = appStatusService.versionCheck();
        return ResponseEntity.ok().body(versionCheckResDto);
    }

    @PostMapping("/new-version")
    public ResponseEntity<Object> insertVersion(@RequestBody NewVersionReqDto newVersionReqDto){
        NewVersionResDto newVersionResDto = appStatusService.insertVersion(newVersionReqDto);
        return ResponseEntity.ok().body(newVersionResDto);
    }

    @PutMapping("/update-version")
    public ResponseEntity<Object> updateVersion(@RequestBody UpdateVersionReqDto updateVersionReqDto){
        UpdateVersionResDto updateVersionResDto = appStatusService.updateVersion(updateVersionReqDto);
        return ResponseEntity.ok().body(updateVersionResDto);
    }
}
