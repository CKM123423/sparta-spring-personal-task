package com.sparta.spartaspringpersonaltask.global.dto.user;

import com.sparta.spartaspringpersonaltask.domain.user.entity.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    private String username;
    private UserRoleEnum role;
}
