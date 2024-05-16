package com.sparta.spartaspringpersonaltask.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.Date;

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
    @Column(name = "schedule_manager", nullable = false)
    private String scheduleManager;
    @Column(name = "schedule_password", nullable = false)
    private String schedulePassword;
    @Column(name = "schedule_datetime", nullable = false)
    private Date scheduleDatetime;

}
