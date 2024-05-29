package com.sparta.spartaspringpersonaltask.domain.user.service;

import com.sparta.spartaspringpersonaltask.domain.user.entity.User;

public interface UserService {
    User getUserByUsername(String username);
}
