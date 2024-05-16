package com.sparta.spartaspringpersonaltask.controller;

import com.sparta.spartaspringpersonaltask.dto.ScheduleRequestDto;
import com.sparta.spartaspringpersonaltask.dto.ScheduleResponseDto;
import com.sparta.spartaspringpersonaltask.service.ScheduleService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PutMapping("/schedule/create")
    public ScheduleResponseDto createSchedule(@RequestBody ScheduleRequestDto scheduleRequestDto) {
        return scheduleService.createSchedule(scheduleRequestDto);
    }
}