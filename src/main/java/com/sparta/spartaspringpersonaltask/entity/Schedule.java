package com.sparta.spartaspringpersonaltask.entity;

import com.sparta.spartaspringpersonaltask.dto.ScheduleRequestDto;
import com.sparta.spartaspringpersonaltask.exceptions.customexceptions.AlreadyDeletedException;
import com.sparta.spartaspringpersonaltask.exceptions.customexceptions.InvalidPasswordException;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Table(name = "schedules")
@NoArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleKey;

    @Column(name = "schedule_title", nullable = false)
    private String scheduleTitle;

    @Column(name = "schedule_content", nullable = false)
    private String scheduleContent;

    @Column(name = "schedule_manager", nullable = false, length = 1000)
    private String scheduleManager;

    @Column(name = "schedule_password", nullable = false)
    private String schedulePassword;

    @Column(name = "schedule_datetime", nullable = false)
    private LocalDateTime scheduleDatetime;

    @Column(name = "schedule_deletionStatus", nullable = false)
    private boolean deletionStatus;

    @Builder
    public Schedule(String scheduleTitle, String scheduleContent, String scheduleManager, String schedulePassword) {
        this.scheduleTitle = scheduleTitle;
        this.scheduleContent = scheduleContent;
        this.scheduleManager = scheduleManager;
        this.schedulePassword = schedulePassword;
        this.scheduleDatetime = LocalDateTime.now();
        this.deletionStatus = false;
    }

    public void update(ScheduleRequestDto requestDto) {
        this.scheduleTitle = requestDto.getScheduleTitle();
        this.scheduleContent = requestDto.getScheduleContent();
        this.scheduleManager = requestDto.getScheduleManager();
        this.scheduleDatetime = LocalDateTime.now();
        this.deletionStatus = false;
    }

    public void markAsDeleted() {
        this.deletionStatus = true;
    }

    public void checkPassword(String password) {
        if (!Objects.equals(this.schedulePassword, password)) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
        }
    }

    public void checkDeletionStatus() {
        if (this.deletionStatus) {
            throw new AlreadyDeletedException("이미 삭제된 일정입니다.");
        }
    }
}