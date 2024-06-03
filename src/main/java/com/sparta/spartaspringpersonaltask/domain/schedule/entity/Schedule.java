package com.sparta.spartaspringpersonaltask.domain.schedule.entity;

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

    @Builder
    public Schedule(User user, String scheduleTitle, String scheduleContent) {
        this.user = user;
        this.scheduleTitle = scheduleTitle;
        this.scheduleContent = scheduleContent;
        this.scheduleDeleteAt = null;
    }

    // 업데이트를 할때 무엇을 업데이트하는지 더 명확하게 하기위해 스케쥴안에서만 동작하도록 변경
    private void update(String scheduleTitle, String scheduleContent) {
        this.scheduleTitle = scheduleTitle;
        this.scheduleContent = scheduleContent;
    }

    // 제목과 내용이 모두 변경될때
    public void updateTitleAndContent(String scheduleTitle, String scheduleContent) {
        update(scheduleTitle, scheduleContent);
    }

    // 제목만 변경될때
    public void updateTitle(String scheduleTitle) {
        update(scheduleTitle, this.scheduleContent);
    }

    // 내용만 변경될때
    public void updateContent(String scheduleContent) {
        update(this.scheduleTitle, scheduleContent);
    }

    public void deletedTime() {
        this.scheduleDeleteAt = LocalDateTime.now();
    }

    public void checkUser(Long userId) {
        if (!Objects.equals(this.user.getUserId(), userId)) {
            throw new InvalidException("유저 정보가 일치하지 않습니다. 작성자만 수정, 삭제가 가능합니다.");
        }
    }
}