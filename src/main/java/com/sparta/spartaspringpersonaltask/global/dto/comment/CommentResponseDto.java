package com.sparta.spartaspringpersonaltask.global.dto.comment;

import com.sparta.spartaspringpersonaltask.domain.comment.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {

    private Long commentId;
    private Long scheduleId;
    private String commentUsername;
    private String commentContent;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getCommentId();
        this.scheduleId = comment.getSchedule().getScheduleId();
        this.commentUsername = comment.getUser().getUserNickname();
        this.commentContent = comment.getCommentContent();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }
}


