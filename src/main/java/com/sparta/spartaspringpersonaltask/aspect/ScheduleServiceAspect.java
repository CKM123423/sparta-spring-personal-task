package com.sparta.spartaspringpersonaltask.aspect;

import com.sparta.spartaspringpersonaltask.dto.ScheduleRequestDto;
import com.sparta.spartaspringpersonaltask.entity.Schedule;
import com.sparta.spartaspringpersonaltask.exceptions.customexceptions.NotFoundException;
import com.sparta.spartaspringpersonaltask.repository.ScheduleRepository;
import com.sparta.spartaspringpersonaltask.utils.ScheduleUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ScheduleServiceAspect {

    private final ScheduleRepository scheduleRepository;

    @Autowired
    public ScheduleServiceAspect(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Before(value =
            "@annotation(com.sparta.spartaspringpersonaltask.aspect.annotation.CheckDeletionStatus) &&" +
                    "args(scheduleKey)",
            argNames = "scheduleKey")
    public void checkDeletionStatus(Long scheduleKey) {
        Schedule schedule = scheduleRepository.findById(scheduleKey)
                .orElseThrow(() -> new NotFoundException("선택한 일정이 없습니다."));
        ScheduleUtils.checkDeletionStatus(schedule);
    }

    @Before(value =
            "@annotation(com.sparta.spartaspringpersonaltask.aspect.annotation.CheckPassword) &&" +
                    "args(scheduleKey, password)",
            argNames = "scheduleKey, password")
    public void checkPassword(Long scheduleKey, String password) {
        Schedule schedule = scheduleRepository.findById(scheduleKey)
                .orElseThrow(() -> new NotFoundException("선택한 일정이 없습니다."));
        ScheduleUtils.checkPassword(password, schedule.getSchedulePassword());
    }

    @Before(value =
            "@annotation(com.sparta.spartaspringpersonaltask.aspect.annotation.CheckPassword) &&" +
                    "args(scheduleKey, requestDto)",
            argNames = "scheduleKey, requestDto")
    public void checkPasswordWithRequestDto(Long scheduleKey, ScheduleRequestDto requestDto) {
        Schedule schedule = scheduleRepository.findById(scheduleKey)
                .orElseThrow(() -> new NotFoundException("선택한 일정이 없습니다."));
        ScheduleUtils.checkPassword(requestDto.getSchedulePassword(), schedule.getSchedulePassword());
    }

}
