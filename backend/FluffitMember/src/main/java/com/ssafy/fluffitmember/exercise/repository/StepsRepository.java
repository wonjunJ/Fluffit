package com.ssafy.fluffitmember.exercise.repository;

import com.ssafy.fluffitmember.exercise.entity.Steps;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface StepsRepository extends JpaRepository<Steps,Integer> {
    Optional<Steps> findByMemberIdAndDate(String memberId, LocalDate date);
}
