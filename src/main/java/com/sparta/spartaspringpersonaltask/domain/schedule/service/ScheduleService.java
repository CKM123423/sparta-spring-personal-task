package com.sparta.spartaspringpersonaltask.domain.schedule.service;

import com.sparta.spartaspringpersonaltask.domain.comment.entity.Comment;
import com.sparta.spartaspringpersonaltask.domain.user.entity.User;
import com.sparta.spartaspringpersonaltask.domain.user.repository.UserRepository;
import com.sparta.spartaspringpersonaltask.global.dto.schedule.ScheduleRequestDto;
import com.sparta.spartaspringpersonaltask.global.dto.schedule.ScheduleResponseDto;
import com.sparta.spartaspringpersonaltask.domain.schedule.entity.Schedule;
import com.sparta.spartaspringpersonaltask.global.exception.customexceptions.NotFoundException;
import com.sparta.spartaspringpersonaltask.domain.schedule.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository, UserRepository userRepository) {
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
    }

    // 일정 등록
    public ScheduleResponseDto createSchedule(ScheduleRequestDto requestDto, String userName) {
        // DTO -> Entity
        User user = findUser(userName);
        Schedule schedule = toSchedule(user, requestDto);

        // DB 저장
        scheduleRepository.save(schedule);

        // Entity -> DTO
        return toDto(schedule);
    }

    // 단일 일정 조회
    public ScheduleResponseDto viewSelectedSchedule(Long scheduleKey) {
        // 일정 유효성 확인 및 객체 생성
        Schedule schedule = retrieveSchedule(scheduleKey);

        // Entity -> DTO
        return toDto(schedule);
    }

    // 전체 일정 조회
    public List<ScheduleResponseDto> viewAllSchedules() {
        return scheduleRepository.findAllByOrderByScheduleDatetimeDesc().stream()
                .filter(schedule -> schedule.getDeletionStatus() == null)
                .map(ScheduleResponseDto::new)
                .toList();
    }

    // 일정 수정
    @Transactional
    public ScheduleResponseDto modifySchedule(Long scheduleKey, ScheduleRequestDto requestDto, String userName) {
        // 일정 유효성 확인 및 객체 생성
        Schedule schedule = retrieveSchedule(scheduleKey);
        User user = findUser(userName);
        Schedule scheduleToUpdate = toSchedule(user, requestDto);

        // 사용자 확인
        schedule.checkUser(scheduleToUpdate.getUser());

        // 수정 내용 저장
        schedule.update(scheduleToUpdate);

        // 수정 내용 조회
        return toDto(schedule);
    }

    // 일정 삭제 기능
    @Transactional
    public String deleteSchedule(Long scheduleKey, String userName) {
        // 일정 존재 여부 확인 및 객체 생성
        Schedule schedule = retrieveSchedule(scheduleKey);
        User user = findUser(userName);

        // 사용자 확인
        schedule.checkUser(user);

        // 일정 삭제 (소프트 삭제)
        schedule.deletedTime();

        // 일정에 연동된 댓글 삭제 (소프트 삭제)
        List<Comment> deleteComment = schedule.getCommentList();

        for (Comment comment : deleteComment) {
            comment.deletedTime();
        }

        return scheduleKey + "번 일정이 삭제 되었습니다.";
    }

    // 일정 찾고 유효성 확인
    private Schedule retrieveSchedule(Long scheduleKey) {
        Schedule schedule = scheduleRepository.findById(scheduleKey).orElseThrow(
                () -> new NotFoundException("해당하는 일정이 없습니다.")
        );
        schedule.checkDeletionStatus();
        return schedule;
    }

    private User findUser(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(
                () -> new NotFoundException("해당하는 유저가 없습니다.")
        );
    }

    private Schedule toSchedule(User user, ScheduleRequestDto requestDto) {
        return Schedule.builder()
                .user(user)
                .scheduleTitle(requestDto.getScheduleTitle())
                .scheduleContent(requestDto.getScheduleContent())
                .build();
    }

    private ScheduleResponseDto toDto(Schedule schedule) {
        return new ScheduleResponseDto(schedule);
    }
}
