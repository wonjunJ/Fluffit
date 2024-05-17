package com.ssafy.fluffitbattle.repository;

import com.ssafy.fluffitbattle.entity.Battle;
import com.ssafy.fluffitbattle.entity.dto.BattleStatisticItemDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BattleRepository extends JpaRepository<Battle, Long> {
//    <T> Mono<T> findByOrganizerIdOrParticipantId(Long userId);

//    Optional<Battle> findByOrganizerIdOrParticipantId(Long id);
    Slice<Battle> findByOrganizerIdOrParticipantIdOrderByBattleDateDesc(String organizerId, String participantId, Pageable pageable);

    @Query("SELECT new com.ssafy.fluffitbattle.entity.dto.BattleStatisticItemDto(b.battleType, COUNT(b), " +
            "SUM(CASE WHEN b.winnerId = :userId THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN b.winnerId != :userId THEN 1 ELSE 0 END)) " +
            "FROM Battle b WHERE b.organizerId = :userId OR b.participantId = :userId " +
            "GROUP BY b.battleType " +
            "HAVING COUNT(b) > 0")
    List<BattleStatisticItemDto> findBattleStatsByUserId(String userId);
}
