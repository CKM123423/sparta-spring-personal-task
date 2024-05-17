package com.sparta.spartaspringpersonaltask.repository;

import com.sparta.spartaspringpersonaltask.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findAllByOrderByScheduleDatetimeDesc();
}
