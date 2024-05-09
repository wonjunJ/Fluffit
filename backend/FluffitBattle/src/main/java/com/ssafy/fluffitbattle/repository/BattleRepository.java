package com.ssafy.fluffitbattle.repository;

import com.ssafy.fluffitbattle.entity.Battle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
public interface BattleRepository extends JpaRepository<Battle, Long> {
//    <T> Mono<T> findByOrganizerIdOrParticipantId(Long userId);

//    Optional<Battle> findByOrganizerIdOrParticipantId(Long id);
}
