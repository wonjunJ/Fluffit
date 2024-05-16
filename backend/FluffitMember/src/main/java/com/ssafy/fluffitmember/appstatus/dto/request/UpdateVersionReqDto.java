package com.ssafy.fluffitmember.appstatus.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateVersionReqDto {
    private String beforeVersion;
    private String updateVersion;
}
