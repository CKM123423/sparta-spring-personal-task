package com.sparta.spartaspringpersonaltask.domain.schedule.repository;

import com.sparta.spartaspringpersonaltask.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findAllByOrderByScheduleDatetimeDesc();
}
