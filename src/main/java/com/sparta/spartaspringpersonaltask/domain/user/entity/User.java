package com.sparta.spartaspringpersonaltask.domain.user.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    @Builder
    public User(String userNickname, String userName, String userPassword, String email, UserRoleEnum role) {
        this.userNickname = userNickname;
        this.userName = userName;
        this.userPassword = userPassword;
        this.email = email;
        this.role = role;
        this.userCreatedDate = LocalDateTime.now();
    }
}
