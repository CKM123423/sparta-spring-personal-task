package com.sparta.spartaspringpersonaltask.global.aop;

import com.sparta.spartaspringpersonaltask.global.dto.schedule.ScheduleRequestDto;
import com.sparta.spartaspringpersonaltask.domain.schedule.entity.Schedule;
import com.sparta.spartaspringpersonaltask.global.exceptions.customexceptions.NotFoundException;
import com.sparta.spartaspringpersonaltask.domain.schedule.repository.ScheduleRepository;
import com.sparta.spartaspringpersonaltask.global.utils.ScheduleUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// 보통 AOP 는 로깅, 보안, 트랜젝션 등 로직외 부가기능을 사용할때 주로 사용함
// 전체적으로 적용되는 부분이 아니고 부분적으로 적용되는 부분이라 AOP 의 의미가 퇴색됨
@Aspect
@Component
@Deprecated
public class ScheduleServiceAspect {

    private final ScheduleRepository scheduleRepository;

    @Autowired
    public ScheduleServiceAspect(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Before(value =
            "@annotation(com.sparta.spartaspringpersonaltask.global.aop.annotation.CheckDeletionStatus) &&" +
                    "args(scheduleKey)",
            argNames = "scheduleKey")
    public void checkDeletionStatus(Long scheduleKey) {
        Schedule schedule = scheduleRepository.findById(scheduleKey)
                .orElseThrow(() -> new NotFoundException("선택한 일정이 없습니다."));
        ScheduleUtils.checkDeletionStatus(schedule);
    }

    @Before(value =
            "@annotation(com.sparta.spartaspringpersonaltask.global.aop.annotation.CheckPassword) &&" +
                    "args(scheduleKey, password)",
            argNames = "scheduleKey, password")
    public void checkPassword(Long scheduleKey, String password) {
        Schedule schedule = scheduleRepository.findById(scheduleKey)
                .orElseThrow(() -> new NotFoundException("선택한 일정이 없습니다."));
        ScheduleUtils.checkPassword(password, schedule.getSchedulePassword());
    }

    @Before(value =
            "@annotation(com.sparta.spartaspringpersonaltask.global.aop.annotation.CheckPassword) &&" +
                    "args(scheduleKey, requestDto)",
            argNames = "scheduleKey, requestDto")
    public void checkPasswordWithRequestDto(Long scheduleKey, ScheduleRequestDto requestDto) {
        Schedule schedule = scheduleRepository.findById(scheduleKey)
                .orElseThrow(() -> new NotFoundException("선택한 일정이 없습니다."));
        ScheduleUtils.checkPassword(requestDto.getSchedulePassword(), schedule.getSchedulePassword());
    }

}
