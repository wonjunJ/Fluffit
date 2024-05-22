package com.ssafy.fluffitmember.exercise.repository;

import com.ssafy.fluffitmember.exercise.entity.Steps;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface StepsRepository extends JpaRepository<Steps,Integer> {
    @Query("SELECT s FROM Steps s WHERE s.memberId = :memberId AND s.date = :date")
    Optional<Steps> findByMemberIdAndDate(String memberId, LocalDate date);
}
