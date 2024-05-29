package com.sparta.spartaspringpersonaltask.domain.schedule.controller;

import com.sparta.spartaspringpersonaltask.domain.schedule.service.ScheduleServiceImpl;
import com.sparta.spartaspringpersonaltask.global.dto.schedule.ScheduleRequestDto;
import com.sparta.spartaspringpersonaltask.global.dto.schedule.ScheduleResponseDto;
import com.sparta.spartaspringpersonaltask.global.auth.security.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ScheduleController {

    private final ScheduleServiceImpl scheduleServiceImpl;

    public ScheduleController(ScheduleServiceImpl scheduleServiceImpl) {
        this.scheduleServiceImpl = scheduleServiceImpl;
    }

    // 일정 등록
    @PostMapping("/schedule/create")
    public ScheduleResponseDto createSchedule(@Valid @RequestBody ScheduleRequestDto requestDto,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String userName = userDetails.getUser().getUserName();

        return scheduleServiceImpl.createSchedule(requestDto, userName);
    }

    // 단일 일정 조회
    @GetMapping("/schedule/{scheduleKey}")
    public ScheduleResponseDto viewSelectedSchedule(@PathVariable Long scheduleKey) {
        return scheduleServiceImpl.viewSelectedSchedule(scheduleKey);
    }

    // 전체 일정 조회
    @GetMapping("schedules")
    public List<ScheduleResponseDto> viewAllSchedules() {
        return scheduleServiceImpl.viewAllSchedules();
    }

    // 선택한 일정 수정
    @PutMapping("/schedule/{scheduleKey}")
    public ScheduleResponseDto modifySchedule(@PathVariable Long scheduleKey,
                                              @Valid @RequestBody ScheduleRequestDto requestDto,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String userName = userDetails.getUser().getUserName();

        return scheduleServiceImpl.modifySchedule(scheduleKey, requestDto, userName);
    }

    // 선택한 일정 삭제
    @DeleteMapping("/schedule/{scheduleKey}")
    public String deleteSchedule(@PathVariable Long scheduleKey,
                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String userName = userDetails.getUser().getUserName();

        return scheduleServiceImpl.deleteSchedule(scheduleKey, userName);
    }

}
