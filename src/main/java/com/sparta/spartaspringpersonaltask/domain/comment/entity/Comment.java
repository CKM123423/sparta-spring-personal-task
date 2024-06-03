package com.sparta.spartaspringpersonaltask.domain.comment.entity;

import com.sparta.spartaspringpersonaltask.domain.schedule.entity.Schedule;
import com.sparta.spartaspringpersonaltask.domain.user.entity.User;
import com.sparta.spartaspringpersonaltask.global.entity.Timestamped;
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
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String commentContent;

    private LocalDateTime commentDeleteAt;

    @Builder
    public Comment(Schedule schedule, User user, String commentContent) {
        this.schedule = schedule;
        this.user = user;
        this.commentContent = commentContent;
        this.commentDeleteAt = null;
    }

    public void updateContent(String commentToUpdate) {
        this.commentContent = commentToUpdate;
    }

    public void deletedTime() {
        this.commentDeleteAt = LocalDateTime.now();
    }

    public void checkUser(Long userId) {
        if (!Objects.equals(this.user.getUserId(), userId)) {
            throw new InvalidException("유저 정보가 일치하지 않습니다. 작성자만 수정, 삭제가 가능합니다.");
        }
    }
}
