package com.sparta.spartaspringpersonaltask.domain.schedule.service;

import com.sparta.spartaspringpersonaltask.domain.schedule.entity.Schedule;
import com.sparta.spartaspringpersonaltask.domain.schedule.repository.ScheduleRepository;
import com.sparta.spartaspringpersonaltask.domain.user.entity.User;
import com.sparta.spartaspringpersonaltask.domain.user.service.UserService;
import com.sparta.spartaspringpersonaltask.global.dto.schedule.ScheduleRequestDto;
import com.sparta.spartaspringpersonaltask.global.dto.schedule.ScheduleResponseDto;
import com.sparta.spartaspringpersonaltask.global.exception.customexceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final UserService userService;

    @Autowired
    public ScheduleServiceImpl(ScheduleRepository scheduleRepository, UserService userService) {
        this.scheduleRepository = scheduleRepository;
        this.userService = userService;
    }

    // 일정 등록
    public ScheduleResponseDto createSchedule(ScheduleRequestDto requestDto, String userName) {
        // 사용자 정보 가져오기
        User user = userService.getUserByUsername(userName);

        // DTO -> Entity
        Schedule schedule = requestDto.toEntity(user);

        // DB 저장
        scheduleRepository.save(schedule);

        // Entity -> DTO
        return new ScheduleResponseDto(schedule);
    }

    // 단일 일정 조회
    public ScheduleResponseDto viewSelectedSchedule(Long scheduleKey) {
        // 일정 유효성 확인 및 객체 생성
        Schedule schedule = retrieveSchedule(scheduleKey);

        // Entity -> DTO
        return new ScheduleResponseDto(schedule);
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

        // 유저 생성 및 유효성 체크
        User user = userService.getUserByUsername(userName);
        validateUserPermission(user, schedule);

        // 비지니스로직용 엔티티 생성
        Schedule scheduleToUpdate = requestDto.toEntity(user);

        // 수정 내용 저장
        schedule.update(scheduleToUpdate);

        // 수정 내용 조회
        return new ScheduleResponseDto(schedule);
    }

    // 일정 삭제 기능
    @Transactional
    public String deleteSchedule(Long scheduleKey, String userName) {
        // 일정 존재 여부 확인 및 객체 생성
        Schedule schedule = retrieveSchedule(scheduleKey);

        // 유저 생성 및 유효성 체크
        User user = userService.getUserByUsername(userName);
        validateUserPermission(user, schedule);

        // 일정 삭제 (소프트 삭제)
        schedule.deletedTime();

        // 일정에 연동된 댓글 삭제 (소프트 삭제)
        schedule.deleteComment();

        return scheduleKey + "번 일정이 삭제 되었습니다.";
    }

    // 일정 찾고 유효성 확인
    @Override
    public Schedule retrieveSchedule(Long scheduleKey) {
        Schedule schedule = scheduleRepository.findById(scheduleKey).orElseThrow(
                () -> new NotFoundException("해당하는 일정이 없습니다.")
        );
        schedule.checkDeletionStatus();
        return schedule;
    }

    private void validateUserPermission(User user, Schedule schedule) {
        if (!user.isAdmin()) {
            user.checkUser(schedule.getUser());
        }
    }
}
