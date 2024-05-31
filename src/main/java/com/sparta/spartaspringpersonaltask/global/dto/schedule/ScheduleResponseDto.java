package com.sparta.spartaspringpersonaltask.global.dto.schedule;

import com.sparta.spartaspringpersonaltask.domain.schedule.entity.Schedule;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleResponseDto {
    private Long scheduleId;
    private String scheduleTitle;
    private String scheduleContent;
    private String scheduleManager;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public ScheduleResponseDto(Schedule schedule) {
        this.scheduleId = schedule.getScheduleId();
        this.scheduleTitle = schedule.getScheduleTitle();
        this.scheduleContent = schedule.getScheduleContent();
        this.scheduleManager = schedule.getUser().getEmail();
        this.createdAt = schedule.getCreatedAt();
        this.modifiedAt = schedule.getModifiedAt();
    }
}
