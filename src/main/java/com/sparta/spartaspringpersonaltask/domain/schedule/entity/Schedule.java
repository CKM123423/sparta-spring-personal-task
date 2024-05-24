package com.sparta.spartaspringpersonaltask.domain.schedule.entity;

import com.sparta.spartaspringpersonaltask.global.dto.ScheduleRequestDto;
import com.sparta.spartaspringpersonaltask.global.exceptions.customexceptions.AlreadyDeletedException;
import com.sparta.spartaspringpersonaltask.global.exceptions.customexceptions.InvalidPasswordException;
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

    @Column(name = "schedule_title", nullable = false, length = 200)
    private String scheduleTitle;

    @Column(name = "schedule_content", nullable = false)
    private String scheduleContent;

    @Column(name = "schedule_manager", nullable = false)
    private String scheduleManager;

    @Column(name = "schedule_password", nullable = false)
    private String schedulePassword;

    @Column(name = "schedule_datetime", nullable = false)
    private LocalDateTime scheduleDatetime;

    @Column(name = "schedule_deletionStatus")
    private LocalDateTime deletionStatus;

    @Builder
    public Schedule(String scheduleTitle, String scheduleContent, String scheduleManager, String schedulePassword) {
        this.scheduleTitle = scheduleTitle;
        this.scheduleContent = scheduleContent;
        this.scheduleManager = scheduleManager;
        this.schedulePassword = schedulePassword;
        this.scheduleDatetime = LocalDateTime.now();
        this.deletionStatus = null;
    }

    public void update(ScheduleRequestDto requestDto) {
        this.scheduleTitle = requestDto.getScheduleTitle();
        this.scheduleContent = requestDto.getScheduleContent();
        this.scheduleManager = requestDto.getScheduleManager();
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