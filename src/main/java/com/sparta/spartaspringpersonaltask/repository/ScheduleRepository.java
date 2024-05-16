package com.sparta.spartaspringpersonaltask.repository;

import com.sparta.spartaspringpersonaltask.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
