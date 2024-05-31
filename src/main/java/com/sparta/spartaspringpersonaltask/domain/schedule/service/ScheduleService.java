package com.sparta.spartaspringpersonaltask.domain.schedule.service;

import com.sparta.spartaspringpersonaltask.domain.schedule.entity.Schedule;
import com.sparta.spartaspringpersonaltask.domain.schedule.repository.ScheduleRepository;
import com.sparta.spartaspringpersonaltask.domain.user.entity.User;
import com.sparta.spartaspringpersonaltask.domain.user.repository.UserRepository;
import com.sparta.spartaspringpersonaltask.global.dto.schedule.ScheduleRequestDto;
import com.sparta.spartaspringpersonaltask.global.dto.schedule.ScheduleResponseDto;
import com.sparta.spartaspringpersonaltask.global.exception.customexceptions.NotFoundException;
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
    public ScheduleResponseDto createSchedule(ScheduleRequestDto requestDto, String username) {
        // 사용자 정보 가져오기
        User user = getUserByUsername(username);

        // DTO -> Entity
        Schedule schedule = requestDto.toEntity(user);

        // DB 저장
        scheduleRepository.save(schedule);

        // Entity -> DTO
        return new ScheduleResponseDto(schedule);
    }

    // 단일 일정 조회
    public ScheduleResponseDto viewSelectedSchedule(Long scheduleId) {
        // 일정 유효성 확인 및 객체 생성
        Schedule schedule = getScheduleByScheduleId(scheduleId);

        // Entity -> DTO
        return new ScheduleResponseDto(schedule);
    }

    // 전체 일정 조회
    public List<ScheduleResponseDto> viewAllSchedules() {
        return scheduleRepository.findAllByScheduleDeleteAtIsNullOrderByCreatedAtDesc().stream()
                .map(ScheduleResponseDto::new)
                .toList();
    }

    // 일정 수정
    @Transactional
    public ScheduleResponseDto modifySchedule(Long scheduleId, ScheduleRequestDto requestDto, String username) {
        // 일정 유효성 확인 및 객체 생성
        Schedule schedule = getScheduleByScheduleId(scheduleId);

        // 로그인 유저 객체 생성
        User user = getUserByUsername(username);

        // 사용자 권한체크
        user.checkAuthority(schedule.getUser().getUserId());

        // 수정 내용 저장
        schedule.update(requestDto.getScheduleTitle(), requestDto.getScheduleContent());

        // 수정 내용 조회
        return new ScheduleResponseDto(schedule);
    }

    // 일정 삭제 기능
    @Transactional
    public String deleteSchedule(Long scheduleId, String username) {
        // 일정 존재 여부 확인 및 객체 생성
        Schedule schedule = getScheduleByScheduleId(scheduleId);

        // 로그인 유저 객체 생성
        User user = getUserByUsername(username);

        // 사용자 권한체크
        user.checkAuthority(schedule.getUser().getUserId());
        
        // 일정에 연동된 댓글 삭제 (소프트 삭제)
        schedule.deleteComment();

        // 일정 삭제 (소프트 삭제)
        schedule.deletedTime();


        return scheduleId + "번 일정이 삭제 되었습니다.";
    }

    // 일정 찾고 유효성 확인
    private Schedule getScheduleByScheduleId(Long scheduleId) {
        return scheduleRepository.findByScheduleIdAndScheduleDeleteAtIsNull(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 일정은 없거나 삭제되었습니다."));
    }

    /**
     * 유저 객체 생성
     * @param username 유저이름 (유니크)
     * @return 유저 객체
     */
    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("해당하는 유저가 없습니다."));
    }

}
