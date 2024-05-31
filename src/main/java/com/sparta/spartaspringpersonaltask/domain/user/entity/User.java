package com.sparta.spartaspringpersonaltask.domain.user.entity;

import com.sparta.spartaspringpersonaltask.domain.comment.entity.Comment;
import com.sparta.spartaspringpersonaltask.domain.schedule.entity.Schedule;
import com.sparta.spartaspringpersonaltask.global.entity.Timestamped;
import com.sparta.spartaspringpersonaltask.global.exception.customexceptions.InvalidException;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String userNickname;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String userPassword;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @OneToMany(mappedBy = "user")
    private List<Comment> commentList;

    @OneToMany(mappedBy = "user")
    private List<Schedule> scheduleList;

    @Builder
    public User(String userNickname, String username, String userPassword, String email, UserRoleEnum role) {
        this.userNickname = userNickname;
        this.username = username;
        this.userPassword = userPassword;
        this.email = email;
        this.role = role;
    }

    public boolean isAdmin() {
        return role.equals(UserRoleEnum.ADMIN);
    }

    public void checkAuthority(String username) {
        if (!this.username.equals(username)) {
            throw new InvalidException("유저 정보가 일치하지 않습니다. 작성자만 수정, 삭제가 가능합니다.");
        }
    }

    public void validatePassword(String userPassword, PasswordEncoder passwordEncoder) {
        boolean valid = passwordEncoder.matches(userPassword, this.userPassword);
        if (!valid) {
            throw new InvalidException("비밀번호가 일치하지 않습니다.");
        }
    }
}
