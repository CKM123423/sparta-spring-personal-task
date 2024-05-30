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

    /**
     * 유저 ID 로 유저객체를 찾아서 전달
     *
     * @param username 유저 ID
     * @return 유저 객체
     */
    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUserName(username).orElseThrow(
                () -> new NotFoundException("해당하는 유저가 없습니다.")
        );
    }

    /**
     * 유저의 권한 확인 로직
     *
     * @param user 변경이나 삭제의 대상이 대는 객체
     * @param entity 요청이 들어온 사용자의 정보가 담긴 객체
     * @param <T> 비교할 객체의 타입
     */
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
