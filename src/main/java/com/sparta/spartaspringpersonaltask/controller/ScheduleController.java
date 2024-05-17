package com.sparta.spartaspringpersonaltask.controller;

import com.sparta.spartaspringpersonaltask.dto.ScheduleRequestDto;
import com.sparta.spartaspringpersonaltask.dto.ScheduleResponseDto;
import com.sparta.spartaspringpersonaltask.service.ScheduleService;
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
    public ScheduleResponseDto createSchedule(@RequestBody ScheduleRequestDto scheduleRequestDto) {
        return scheduleService.createSchedule(scheduleRequestDto);
    }

    // 단일 일정 조회
    @GetMapping("/schedule/{scheduleKey}")
    public ScheduleResponseDto viewSelectedSchedule(@PathVariable Long scheduleKey) {
        return scheduleService.viewSelectedSchedule(scheduleKey);
    }

    // 전체 일정 조회
    @GetMapping("/schedules")
    public List<ScheduleResponseDto> viewAllSchedules() {
        return scheduleService.viewAllSchedules();
    }

    // 선택한 일정 수정
    @PutMapping("/schedule/{scheduleKey}")
    public ScheduleResponseDto modifySchedule(@PathVariable Long scheduleKey, @RequestBody ScheduleRequestDto requestDto) {
        return scheduleService.modifySchedule(scheduleKey, requestDto);
    }

    // 선택한 일정 삭제
    @DeleteMapping("/schedule/{scheduleKey}")
    public Long deleteSchedule(@PathVariable Long scheduleKey, @RequestBody ScheduleRequestDto requestDto) {
        return scheduleService.deleteSchedule(scheduleKey, requestDto);
    }

}