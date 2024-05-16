package com.ssafy.fluffitmember.appstatus.repository;

import com.ssafy.fluffitmember.appstatus.entity.AppStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AppStatusRepository extends JpaRepository<AppStatus,Integer> {

    @Query("SELECT a FROM AppStatus a ORDER BY a.id DESC")
    List<AppStatus> findLatestAppStatus(Pageable pageable);

    Optional<AppStatus> findAppStatusByVersion(String version);
}
