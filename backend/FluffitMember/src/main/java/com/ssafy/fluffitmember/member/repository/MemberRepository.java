package com.ssafy.fluffitmember.member.repository;

import com.ssafy.fluffitmember.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Integer> {
    Optional<Member> findBySocialId(String encryptedData);

    Optional<Member> findByMemberId(String memberId);

    Optional<Member> findByNickname(String nickname);

    @Query(value = "SELECT ranked.rank " +
            "FROM ( " +
            "    SELECT " +
            "        m.member_id, " +
            "        RANK() OVER (ORDER BY m.battle_point DESC, battle_point_update_date ASC) AS rank " +
            "    FROM " +
            "        member m " +
            ") AS ranked " +
            "WHERE ranked.member_id = :memberId",
            nativeQuery = true)
    Optional<Integer> findRankByMemberId(@Param("memberId") String memberId);

    @Query(value = "SELECT * FROM member ORDER BY battle_point DESC, battle_point_update_date ASC LIMIT 3", nativeQuery = true)
    List<Member> findTop3ByBattlePoint();

    @Query(value = "SELECT RANK() OVER (ORDER BY m.battle_point DESC, battle_point_update_date ASC) AS ranking FROM member m LIMIT 3", nativeQuery = true)
    List<Integer> findTop3RankingsByBattlePoint();
}

