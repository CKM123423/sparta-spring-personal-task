package com.sparta.spartaspringpersonaltask.domain.user.service;

import com.sparta.spartaspringpersonaltask.domain.user.entity.User;

public interface UserService {
    // 유저 정보 조회해서 반환
    User getUserByUsername(String username);
    // 유저 권한 확인
    <T> void validateUserPermission(User user, T entity);

}
