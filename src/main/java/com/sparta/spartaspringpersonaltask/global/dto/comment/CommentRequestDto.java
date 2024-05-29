package com.sparta.spartaspringpersonaltask.global.dto.comment;

import com.sparta.spartaspringpersonaltask.domain.comment.entity.Comment;
import com.sparta.spartaspringpersonaltask.domain.schedule.entity.Schedule;
import com.sparta.spartaspringpersonaltask.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentRequestDto {

    @NotBlank(message = "댓글 내용을 필수 입력 값입니다")
    private String commentContent;

    public Comment toEntity(Schedule schedule, User user) {
        return Comment.builder()
                .schedule(schedule)
                .user(user)
                .commentContent(this.getCommentContent())
                .build();
    }
}
