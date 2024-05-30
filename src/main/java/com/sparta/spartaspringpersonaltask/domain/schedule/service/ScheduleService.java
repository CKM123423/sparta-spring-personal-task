package com.sparta.spartaspringpersonaltask.domain.schedule.service;

import com.sparta.spartaspringpersonaltask.domain.schedule.entity.Schedule;

public interface ScheduleService {
    Schedule retrieveSchedule(Long scheduleKey);
}
