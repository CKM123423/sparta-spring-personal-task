package com.sparta.spartaspringpersonaltask.domain.schedule.entity;

import com.sparta.spartaspringpersonaltask.domain.comment.entity.Comment;
import com.sparta.spartaspringpersonaltask.global.exceptions.customexceptions.AlreadyDeletedException;
import com.sparta.spartaspringpersonaltask.global.exceptions.customexceptions.InvalidPasswordException;
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

    @Column(nullable = false)
    private String scheduleManager;

    @Column(nullable = false)
    private String schedulePassword;

    @Column(nullable = false)
    private LocalDateTime scheduleDatetime;

    private LocalDateTime deletionStatus;

    @OneToMany(mappedBy = "schedule")
    private List<Comment> commentList;

    @Builder
    public Schedule(String scheduleTitle, String scheduleContent, String scheduleManager, String schedulePassword) {
        this.scheduleTitle = scheduleTitle;
        this.scheduleContent = scheduleContent;
        this.scheduleManager = scheduleManager;
        this.schedulePassword = schedulePassword;
        this.scheduleDatetime = LocalDateTime.now();
        this.deletionStatus = null;
    }

    public void update(Schedule schedule) {
        this.scheduleTitle = schedule.getScheduleTitle();
        this.scheduleContent = schedule.getScheduleContent();
        this.scheduleManager = schedule.getScheduleManager();
        this.scheduleDatetime = LocalDateTime.now();
    }

    public void deletedTime() {
        this.deletionStatus = LocalDateTime.now();
    }

    public void checkPassword(String password) {
        if (!Objects.equals(this.schedulePassword, password)) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
        }
    }

    public void checkDeletionStatus() {
        if (this.deletionStatus != null) {
            throw new AlreadyDeletedException("이미 삭제된 일정입니다.");
        }
    }
}