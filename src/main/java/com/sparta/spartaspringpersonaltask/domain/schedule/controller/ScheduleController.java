package com.sparta.spartaspringpersonaltask.domain.schedule.controller;

import com.sparta.spartaspringpersonaltask.domain.schedule.service.ScheduleService;
import com.sparta.spartaspringpersonaltask.domain.user.entity.UserRoleEnum;
import com.sparta.spartaspringpersonaltask.global.auth.security.UserDetailsImpl;
import com.sparta.spartaspringpersonaltask.global.dto.schedule.ScheduleRequestDto;
import com.sparta.spartaspringpersonaltask.global.dto.schedule.ScheduleResponseDto;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    // 일정 등록
    @PostMapping
    public ScheduleResponseDto createSchedule(@Valid @RequestBody ScheduleRequestDto requestDto,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.getUser().getUserId();

        return scheduleService.createSchedule(requestDto, userId);
    }

    // 단일 일정 조회
    @GetMapping("/{scheduleId}")
    public ScheduleResponseDto viewSelectedSchedule(@PathVariable(name = "scheduleId") Long scheduleId) {
        return scheduleService.viewSelectedSchedule(scheduleId);
    }

    // 전체 일정 조회
    @GetMapping
    public List<ScheduleResponseDto> viewAllSchedules() {
        return scheduleService.viewAllSchedules();
    }

    // 선택한 일정 수정
    @PutMapping("/{scheduleId}")
    public ScheduleResponseDto modifySchedule(@PathVariable(name = "scheduleId") Long scheduleId,
                                              @Valid @RequestBody ScheduleRequestDto requestDto,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.getUser().getUserId();
        UserRoleEnum role = userDetails.getUser().getRole();

        return scheduleService.modifySchedule(scheduleId, requestDto, userId, role);
    }

    // 선택한 일정 삭제
    @DeleteMapping("/{scheduleId}")
    public String deleteSchedule(@PathVariable(name = "scheduleId") Long scheduleId,
                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.getUser().getUserId();
        UserRoleEnum role = userDetails.getUser().getRole();

        return scheduleService.deleteSchedule(scheduleId, userId, role);
    }
}
