package com.ssafy.fluffitmember.appstatus.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class AppStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDate updateDate;

    @Column(nullable = false)
    private String version;

    @Builder
    private AppStatus(String version){
        this.version = version;
    }

    public static AppStatus of(String version){
        return builder()
                .version(version)
                .build();
    }

    public void updateVersion(String updateVersion) {
        this.version = updateVersion;
    }
}
