package com.sparta.spartaspringpersonaltask.global.dto.schedule;

import com.sparta.spartaspringpersonaltask.domain.schedule.entity.Schedule;
import com.sparta.spartaspringpersonaltask.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ScheduleRequestDto {
    @NotBlank(message = "제목은 필수 입력 값입니다.")
    @Size(max = 200, message = "제목은 200자 이내로 입력해주세요.")
    private String scheduleTitle;

    private String scheduleContent;

    public Schedule toEntity(User user) {
        return Schedule.builder()
                .user(user)
                .scheduleTitle(this.getScheduleTitle())
                .scheduleContent(this.getScheduleContent())
                .build();
    }
}
