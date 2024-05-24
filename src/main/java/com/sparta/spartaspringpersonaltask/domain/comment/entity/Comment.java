package com.sparta.spartaspringpersonaltask.domain.comment.entity;

import com.sparta.spartaspringpersonaltask.domain.schedule.entity.Schedule;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    @Builder
    public Comment(Schedule schedule, String commentUserName, String commentContent) {
        this.schedule = schedule;
        this.commentUserName = commentUserName;
        this.commentContent = commentContent;
        this.commentDatetime = LocalDateTime.now();
    }
}
