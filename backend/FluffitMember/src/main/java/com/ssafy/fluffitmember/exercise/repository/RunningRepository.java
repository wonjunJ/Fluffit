package com.ssafy.fluffitmember.exercise.repository;

import com.ssafy.fluffitmember.exercise.entity.Running;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RunningRepository extends JpaRepository<Running,Integer> {
}
