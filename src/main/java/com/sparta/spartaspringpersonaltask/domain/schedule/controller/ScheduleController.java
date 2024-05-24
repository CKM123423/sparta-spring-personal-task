package com.sparta.spartaspringpersonaltask.domain.schedule.controller;

import com.sparta.spartaspringpersonaltask.global.dto.ScheduleDeleteRequestDto;
import com.sparta.spartaspringpersonaltask.global.dto.ScheduleRequestDto;
import com.sparta.spartaspringpersonaltask.global.dto.ScheduleResponseDto;
import com.sparta.spartaspringpersonaltask.domain.schedule.service.ScheduleService;
import jakarta.validation.Valid;
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
    public ScheduleResponseDto createSchedule(@Valid @RequestBody ScheduleRequestDto scheduleRequestDto) {
        return scheduleService.createSchedule(scheduleRequestDto);
    }

    // 단일 일정 조회
    @GetMapping("/schedule/{scheduleKey}")
    public ScheduleResponseDto viewSelectedSchedule(@PathVariable Long scheduleKey) {
        return scheduleService.viewSelectedSchedule(scheduleKey);
    }

    // 전체 일정 조회
    @GetMapping("schedules")
    public List<ScheduleResponseDto> viewAllSchedules() {
        return scheduleService.viewAllSchedules();
    }

    // 선택한 일정 수정
    @PutMapping("/schedule/{scheduleKey}")
    public ScheduleResponseDto modifySchedule(@PathVariable Long scheduleKey, @Valid @RequestBody ScheduleRequestDto requestDto) {
        return scheduleService.modifySchedule(scheduleKey, requestDto);
    }

    // 선택한 일정 삭제
    @DeleteMapping("/schedule/{scheduleKey}")
    public String deleteSchedule(@PathVariable Long scheduleKey, @Valid @RequestBody ScheduleDeleteRequestDto requestDto) {
        return scheduleService.deleteSchedule(scheduleKey, requestDto);
    }

}
