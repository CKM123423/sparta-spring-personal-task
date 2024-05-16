package com.sparta.spartaspringpersonaltask.dto;

import lombok.Getter;

@Getter
public class ScheduleRequestDto {
    private String scheduleTitle;
    private String scheduleContent;
    private String scheduleManager;
    private String schedulePassword;
}
