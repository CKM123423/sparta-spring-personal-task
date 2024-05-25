package com.sparta.spartaspringpersonaltask.domain.comment.entity;

import com.sparta.spartaspringpersonaltask.domain.schedule.entity.Schedule;
import com.sparta.spartaspringpersonaltask.global.exceptions.customexceptions.InvalidUserNameException;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Table(name = "comment")
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentKey;

    @ManyToOne
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
            throw new InvalidUserNameException("사용자 이름이 맞지 않습니다.");
        }
    }
}
