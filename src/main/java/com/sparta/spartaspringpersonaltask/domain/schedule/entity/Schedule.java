package com.sparta.spartaspringpersonaltask.domain.schedule.entity;

import com.sparta.spartaspringpersonaltask.domain.comment.entity.Comment;
import com.sparta.spartaspringpersonaltask.domain.user.entity.User;
import com.sparta.spartaspringpersonaltask.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Table(name = "schedules")
@NoArgsConstructor
public class Schedule extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;

    @Column(nullable = false, length = 200)
    private String scheduleTitle;

    private String scheduleContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime scheduleDeleteAt;

    @OneToMany(mappedBy = "schedule")
    private List<Comment> commentList;

    @Builder
    public Schedule(User user, String scheduleTitle, String scheduleContent) {
        this.user = user;
        this.scheduleTitle = scheduleTitle;
        this.scheduleContent = scheduleContent;
        this.scheduleDeleteAt = null;
    }

    public void update(String scheduleTitle, String scheduleContent) {
        this.scheduleTitle = scheduleTitle;
        this.scheduleContent = scheduleContent;
    }

    public void deletedTime() {
        this.scheduleDeleteAt = LocalDateTime.now();
    }

    public void deleteComment() {
        for (Comment comment : this.commentList) {
            if (comment.getCommentDeleteAt() == null){
                comment.deletedTime();
            }
        }
    }
}