package com.sparta.spartaspringpersonaltask.global.dto.user;

import com.sparta.spartaspringpersonaltask.domain.user.entity.User;
import com.sparta.spartaspringpersonaltask.domain.user.entity.UserRoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String userNickname;

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d).{4,10}$",
            message = "아이디는 최소 4자 이상, 10자 이하이며 알파벳 소문자와 숫자로 구성되어야 합니다.")
    private String userName;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,15}$",
            message = "비밀번호는 최소 8자 이상, 15자 이하이며 알파벳 대소문자와 숫자로 구성되어야 합니다.")
    private String userPassword;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "유효한 이메일 형식이어야 합니다")
    private String email;
    private boolean admin = false;
    private String adminToken = "";

    public User toEntity(String encodedPassword, UserRoleEnum role) {
        return User.builder()
                .userNickname(this.getUserNickname())
                .userName(this.getUserName())
                .userPassword(encodedPassword)
                .email(this.getEmail())
                .role(role)
                .build();
    }
}
