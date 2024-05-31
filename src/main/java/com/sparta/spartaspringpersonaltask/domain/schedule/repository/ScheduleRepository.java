package com.sparta.spartaspringpersonaltask.domain.schedule.repository;

import com.sparta.spartaspringpersonaltask.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    Optional<Schedule> findByScheduleIdAndScheduleDeleteAtIsNull(Long scheduleId);

    List<Schedule> findAllByScheduleDeleteAtIsNullOrderByCreatedAtDesc();
}
