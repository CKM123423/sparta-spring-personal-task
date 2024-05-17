package com.sparta.spartaspringpersonaltask.dto;

import com.sparta.spartaspringpersonaltask.entity.Schedule;
import lombok.Getter;

@Getter
public class ScheduleRequestDto {
    private String scheduleTitle;
    private String scheduleContent;
    private String scheduleManager;
    private String schedulePassword;

    public Schedule toEntity() {
        return Schedule.builder()
                .scheduleTitle(scheduleTitle)
                .scheduleContent(scheduleContent)
                .scheduleManager(scheduleManager)
                .schedulePassword(schedulePassword)
                .build();
    }
}
