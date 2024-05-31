package com.sparta.spartaspringpersonaltask.domain.schedule.controller;

import com.sparta.spartaspringpersonaltask.domain.schedule.service.ScheduleService;
import com.sparta.spartaspringpersonaltask.global.dto.schedule.ScheduleRequestDto;
import com.sparta.spartaspringpersonaltask.global.dto.schedule.ScheduleResponseDto;
import com.sparta.spartaspringpersonaltask.global.auth.security.UserDetailsImpl;
import com.sparta.spartaspringpersonaltask.global.dto.user.UserRequestDto;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    // 일정 등록
    @PostMapping("/schedule/create")
    public ScheduleResponseDto createSchedule(@Valid @RequestBody ScheduleRequestDto requestDto,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUser().getUsername();

        return scheduleService.createSchedule(requestDto, username);
    }

    // 단일 일정 조회
    @GetMapping("/schedule/{scheduleId}")
    public ScheduleResponseDto viewSelectedSchedule(@PathVariable(name = "scheduleId") Long scheduleId) {
        return scheduleService.viewSelectedSchedule(scheduleId);
    }

    // 전체 일정 조회
    @GetMapping("schedules")
    public List<ScheduleResponseDto> viewAllSchedules() {
        return scheduleService.viewAllSchedules();
    }

    // 선택한 일정 수정
    @PutMapping("/schedule/{scheduleId}")
    public ScheduleResponseDto modifySchedule(@PathVariable(name = "scheduleId") Long scheduleId,
                                              @Valid @RequestBody ScheduleRequestDto requestDto,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return scheduleService.modifySchedule(scheduleId, requestDto, userInfo(userDetails));
    }

    // 선택한 일정 삭제
    @DeleteMapping("/schedule/{scheduleId}")
    public String deleteSchedule(@PathVariable(name = "scheduleId") Long scheduleId,
                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return scheduleService.deleteSchedule(scheduleId, userInfo(userDetails));
    }

    /**
     * 유저의 권한을 확인하기 위한 정보전달 객체
     *
     * @param userDetails 유저의 정보
     * @return 유저의 정보전달 객체
     */
    private UserRequestDto userInfo(UserDetailsImpl userDetails) {
        return new UserRequestDto(userDetails.getUser().getUsername(), userDetails.getUser().getRole());
    }
}
