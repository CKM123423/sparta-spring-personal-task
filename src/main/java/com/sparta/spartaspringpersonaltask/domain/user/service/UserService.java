package com.sparta.spartaspringpersonaltask.domain.user.service;

import com.sparta.spartaspringpersonaltask.domain.user.entity.User;

/**
 * 다른 도메인에서 유저정보가 필요할때 주입받는 인터페이스
 */
public interface UserService {
    User getUserByUsername(String username);
    <T> void validateUserPermission(User user, T entity);

}
