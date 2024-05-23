package com.sparta.spartaspringpersonaltask.global.dto;

import com.sparta.spartaspringpersonaltask.domain.schedule.entity.Schedule;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleResponseDto {
    private Long scheduleKey;
    private String scheduleTitle;
    private String scheduleContent;
    private String scheduleManager;
    private LocalDateTime scheduleDatetime;

    public ScheduleResponseDto(Schedule schedule) {
        this.scheduleKey = schedule.getScheduleKey();
        this.scheduleTitle = schedule.getScheduleTitle();
        this.scheduleContent = schedule.getScheduleContent();
        this.scheduleManager = schedule.getScheduleManager();
        this.scheduleDatetime = schedule.getScheduleDatetime();
    }
}
