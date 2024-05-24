package com.sparta.spartaspringpersonaltask.global.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ScheduleRequestDto {
    @NotBlank(message = "제목은 필수 입력 값입니다.")
    @Size(max = 200, message = "제목은 200자 이내로 입력해주세요.")
    private String scheduleTitle;

    @NotBlank(message = "내용은 필수 입력 값입니다.")
    private String scheduleContent;

    @NotBlank(message = "담당자는 필수 입력 값입니다.")
    @Email(message = "담당자는 이메일 형식이어야 합니다.")
    private String scheduleManager;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String schedulePassword;
}
