package com.sparta.spartaspringpersonaltask.global.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentDeleteRequestDto {
    @NotBlank(message = "댓글 작성자는 필수 입력 값입니다")
    private String userName;
}
