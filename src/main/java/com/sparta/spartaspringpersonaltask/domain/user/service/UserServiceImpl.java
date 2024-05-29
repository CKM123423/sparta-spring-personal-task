package com.sparta.spartaspringpersonaltask.domain.user.service;

import com.sparta.spartaspringpersonaltask.domain.comment.entity.Comment;
import com.sparta.spartaspringpersonaltask.domain.schedule.entity.Schedule;
import com.sparta.spartaspringpersonaltask.domain.user.entity.User;
import com.sparta.spartaspringpersonaltask.domain.user.repository.UserRepository;
import com.sparta.spartaspringpersonaltask.global.exception.customexceptions.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUserName(username).orElseThrow(
                () -> new NotFoundException("해당하는 유저가 없습니다.")
        );
    }

    @Override
    public <T> void validateUserPermission(User user, T entity) {
        if (!user.isAdmin()) {
            if (entity instanceof Schedule schedule) {
                user.checkUser(schedule.getUser());
            } else if (entity instanceof Comment comment) {
                user.checkUser(comment.getUser());
            } else {
                throw new UnsupportedOperationException("지원하지 않는 요청입니다.");
            }
        }
    }
}
