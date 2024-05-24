package com.sparta.spartaspringpersonaltask.global.dto.comment;

import com.sparta.spartaspringpersonaltask.domain.comment.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {

    private Long commentKey;
    private Long scheduleId;
    private String commentUserName;
    private String commentContent;
    private LocalDateTime commentDatetime;

    public CommentResponseDto(Comment comment) {
        this.commentKey = comment.getCommentKey();
        this.scheduleId = comment.getSchedule().getScheduleKey();
        this.commentUserName = comment.getCommentUserName();
        this.commentContent = comment.getCommentContent();
        this.commentDatetime = comment.getCommentDatetime();
    }
}


