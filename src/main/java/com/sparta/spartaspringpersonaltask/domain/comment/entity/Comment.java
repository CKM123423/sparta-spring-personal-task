package com.sparta.spartaspringpersonaltask.domain.comment.entity;

import com.sparta.spartaspringpersonaltask.domain.schedule.entity.Schedule;
import com.sparta.spartaspringpersonaltask.domain.user.entity.User;
import com.sparta.spartaspringpersonaltask.domain.user.entity.UserRoleEnum;
import com.sparta.spartaspringpersonaltask.global.exception.customexceptions.AlreadyDeletedException;
import com.sparta.spartaspringpersonaltask.global.exception.customexceptions.InvalidException;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Table(name = "comments")
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_key")
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String commentContent;

    @Column(nullable = false)
    private LocalDateTime commentDatetime;

    private LocalDateTime commentDeletionStatus;

    @Builder
    public Comment(Schedule schedule, User user, String commentContent) {
        this.schedule = schedule;
        this.user = user;
        this.commentContent = commentContent;
        this.commentDatetime = LocalDateTime.now();
        this.commentDeletionStatus = null;
    }

    public void update(Comment commentToUpdate) {
        this.commentContent = commentToUpdate.getCommentContent();
        this.commentDatetime = LocalDateTime.now();
    }

    public void checkDeletionStatus() {
        if (this.commentDeletionStatus != null) {
            throw new AlreadyDeletedException("이미 삭제된 댓글입니다.");
        }
    }

    public void deletedTime() {
        this.commentDeletionStatus = LocalDateTime.now();
    }
}
