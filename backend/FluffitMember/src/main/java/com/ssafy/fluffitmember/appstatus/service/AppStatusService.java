package com.ssafy.fluffitmember.appstatus.service;


import com.ssafy.fluffitmember.appstatus.dto.request.NewVersionReqDto;
import com.ssafy.fluffitmember.appstatus.dto.request.UpdateVersionReqDto;
import com.ssafy.fluffitmember.appstatus.dto.response.NewVersionResDto;
import com.ssafy.fluffitmember.appstatus.dto.response.UpdateVersionResDto;
import com.ssafy.fluffitmember.appstatus.dto.response.VersionCheckResDto;
import com.ssafy.fluffitmember.appstatus.entity.AppStatus;
import com.ssafy.fluffitmember.appstatus.repository.AppStatusRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AppStatusService {

    private final AppStatusRepository appStatusRepository;

    public VersionCheckResDto versionCheck() {
        List<AppStatus> appStatuses = appStatusRepository.findLatestAppStatus(PageRequest.of(0, 1));
        if (appStatuses.isEmpty()) {
            throw new EntityNotFoundException("No AppStatus found");
        }
        return new VersionCheckResDto(appStatuses.get(0).getVersion());
    }

    public NewVersionResDto insertVersion(NewVersionReqDto newVersionReqDto) {
        AppStatus appStatus = AppStatus.of(newVersionReqDto.getVersion());
        AppStatus save = appStatusRepository.save(appStatus);
        return new NewVersionResDto(save.getVersion());
    }

    @Transactional
    public UpdateVersionResDto updateVersion(UpdateVersionReqDto updateVersionReqDto) {
        Optional<AppStatus> appStatusByVersion = appStatusRepository.findAppStatusByVersion(
                updateVersionReqDto.getBeforeVersion());
        if (appStatusByVersion.isEmpty()) {
            throw new EntityNotFoundException("No AppStatus found");
        }

        AppStatus appStatus = appStatusByVersion.get();
        appStatus.updateVersion(updateVersionReqDto.getUpdateVersion());
        appStatusRepository.save(appStatus);
        return new UpdateVersionResDto(appStatus.getVersion());
    }
}
