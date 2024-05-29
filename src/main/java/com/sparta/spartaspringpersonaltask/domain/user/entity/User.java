package com.sparta.spartaspringpersonaltask.domain.user.entity;

import com.sparta.spartaspringpersonaltask.domain.comment.entity.Comment;
import com.sparta.spartaspringpersonaltask.domain.schedule.entity.Schedule;
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
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userKey;

    @Column(nullable = false)
    private String userNickname;

    @Column(nullable = false, unique = true)
    private String userName;

    @Column(nullable = false)
    private String userPassword;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Column(nullable = false)
    private LocalDateTime userCreatedDate;

    @OneToMany(mappedBy = "user")
    private List<Comment> commentList;

    @OneToMany(mappedBy = "user")
    private List<Schedule> scheduleList;

    @Builder
    public User(String userNickname, String userName, String userPassword, String email, UserRoleEnum role) {
        this.userNickname = userNickname;
        this.userName = userName;
        this.userPassword = userPassword;
        this.email = email;
        this.role = role;
        this.userCreatedDate = LocalDateTime.now();
    }

    public boolean isAdmin() {
        return role.equals(UserRoleEnum.ADMIN);
    }

    public void checkUser(User user) {
        if (!Objects.equals(this.getUserName(), user.getUserName())) {
            throw new InvalidException("유저 정보가 일치하지 않습니다. 작성자만 수정, 삭제가 가능합니다.");
        }
    }
}
