package com.sparta.spartaspringpersonaltask.global.dto.schedule;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ScheduleDeleteRequestDto {

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String schedulePassword;
}
