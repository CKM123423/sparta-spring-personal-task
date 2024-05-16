package com.sparta.spartaspringpersonaltask.entity;

import com.sparta.spartaspringpersonaltask.dto.ScheduleRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
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

    public Schedule(ScheduleRequestDto scheduleRequestDto) {
        this.scheduleTitle = scheduleRequestDto.getScheduleTitle();
        this.scheduleContent = scheduleRequestDto.getScheduleContent();
        this.scheduleManager = scheduleRequestDto.getScheduleManager();
        this.schedulePassword = scheduleRequestDto.getSchedulePassword();
        this.scheduleDatetime = LocalDateTime.now();
    }

    public void update(ScheduleRequestDto requestDto) {
        this.scheduleTitle = requestDto.getScheduleTitle();
        this.scheduleContent = requestDto.getScheduleContent();
        this.scheduleManager = requestDto.getScheduleManager();
        this.scheduleDatetime = LocalDateTime.now();
    }
}