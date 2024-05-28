package com.sparta.spartaspringpersonaltask.global.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentRequestDto {

    @NotBlank(message = "댓글 내용을 필수 입력 값입니다")
    private String commentContent;
}
