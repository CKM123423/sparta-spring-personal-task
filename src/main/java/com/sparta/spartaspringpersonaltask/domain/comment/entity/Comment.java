package com.sparta.spartaspringpersonaltask.domain.comment.entity;

import com.sparta.spartaspringpersonaltask.domain.schedule.entity.Schedule;
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

    @Column(nullable = false)
    private String commentUserName;

    @Column(nullable = false)
    private String commentContent;

    @Column(nullable = false)
    private LocalDateTime commentDatetime;

    private LocalDateTime commentDeletionStatus;

    @Builder
    public Comment(Schedule schedule, String commentUserName, String commentContent) {
        this.schedule = schedule;
        this.commentUserName = commentUserName;
        this.commentContent = commentContent;
        this.commentDatetime = LocalDateTime.now();
        this.commentDeletionStatus = null;
    }

    public void update(Comment commentToUpdate) {
        this.commentContent = commentToUpdate.getCommentContent();
        this.commentDatetime = LocalDateTime.now();
    }

    public void checkUserName(String commentUserName) {
        if (!Objects.equals(this.commentUserName, commentUserName)) {
            throw new InvalidException("사용자 이름이 일치하지 않습니다.");
        }
    }

    public void checkDeletionStatus() {
        if (this.schedule.getDeletionStatus() != null){
            throw new AlreadyDeletedException("이미 삭제된 일정입니다.");
        }

        if (this.commentDeletionStatus != null) {
            throw new AlreadyDeletedException("이미 삭제된 댓글입니다.");
        }
    }

    public void deletedTime() {
        this.commentDeletionStatus = LocalDateTime.now();
    }
}
