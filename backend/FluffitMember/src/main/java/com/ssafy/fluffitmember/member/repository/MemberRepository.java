package com.ssafy.fluffitmember.member.repository;

import com.ssafy.fluffitmember.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Integer> {
    Optional<Member> findBySocialId(String encryptedData);
}
