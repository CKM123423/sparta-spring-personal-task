package com.sparta.spartaspringpersonaltask.domain.schedule.entity;

import com.sparta.spartaspringpersonaltask.domain.comment.entity.Comment;
import com.sparta.spartaspringpersonaltask.domain.user.entity.User;
import com.sparta.spartaspringpersonaltask.domain.user.entity.UserRoleEnum;
import com.sparta.spartaspringpersonaltask.global.dto.schedule.ScheduleResponseDto;
import com.sparta.spartaspringpersonaltask.global.exception.customexceptions.AlreadyDeletedException;
import com.sparta.spartaspringpersonaltask.global.exception.customexceptions.InvalidException;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Table(name = "schedules")
@NoArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleKey;

    @Column(nullable = false, length = 200)
    private String scheduleTitle;

    private String scheduleContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private LocalDateTime scheduleDatetime;

    private LocalDateTime deletionStatus;

    @OneToMany(mappedBy = "schedule")
    private List<Comment> commentList;

    @Builder
    public Schedule(User user, String scheduleTitle, String scheduleContent) {
        this.user = user;
        this.scheduleTitle = scheduleTitle;
        this.scheduleContent = scheduleContent;
        this.scheduleDatetime = LocalDateTime.now();
        this.deletionStatus = null;
    }

    public void update(Schedule schedule) {
        this.scheduleTitle = schedule.getScheduleTitle();
        this.scheduleContent = schedule.getScheduleContent();
        this.scheduleDatetime = LocalDateTime.now();
    }

    public void deletedTime() {
        this.deletionStatus = LocalDateTime.now();
    }

    public void deleteComment() {
        for (Comment comment : commentList) {
            if (comment.getCommentDeletionStatus() == null){
                comment.deletedTime();
            }
        }
    }


    public void checkDeletionStatus() {
        if (this.deletionStatus != null) {
            throw new AlreadyDeletedException("이미 삭제된 일정입니다.");
        }
    }
}