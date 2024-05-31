package com.sparta.spartaspringpersonaltask.domain.schedule.service;

import com.sparta.spartaspringpersonaltask.domain.schedule.entity.Schedule;
import com.sparta.spartaspringpersonaltask.domain.schedule.repository.ScheduleRepository;
import com.sparta.spartaspringpersonaltask.domain.user.entity.User;
import com.sparta.spartaspringpersonaltask.domain.user.entity.UserRoleEnum;
import com.sparta.spartaspringpersonaltask.domain.user.repository.UserRepository;
import com.sparta.spartaspringpersonaltask.global.dto.schedule.ScheduleRequestDto;
import com.sparta.spartaspringpersonaltask.global.dto.schedule.ScheduleResponseDto;
import com.sparta.spartaspringpersonaltask.global.dto.user.UserRequestDto;
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
        User user = getUser(username);

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
        Schedule schedule = getSchedule(scheduleId);

        // Entity -> DTO
        return new ScheduleResponseDto(schedule);
    }

    // 전체 일정 조회
    public List<ScheduleResponseDto> viewAllSchedules() {
        return scheduleRepository.findAllAndDeletedAtIsNullByOrderByCreatedAtDesc().stream()
                .map(ScheduleResponseDto::new)
                .toList();
    }

    // 일정 수정
    @Transactional
    public ScheduleResponseDto modifySchedule(Long scheduleId, ScheduleRequestDto requestDto, UserRequestDto userRequestDto) {
        // 일정 유효성 확인 및 객체 생성
        Schedule schedule = getSchedule(scheduleId);

        // 사용자 권한체크
        checkUserAuthority(userRequestDto, schedule);

        // 수정 내용 저장
        schedule.update(requestDto.getScheduleTitle(), requestDto.getScheduleContent());

        // 수정 내용 조회
        return new ScheduleResponseDto(schedule);
    }

    // 일정 삭제 기능
    @Transactional
    public String deleteSchedule(Long scheduleId, UserRequestDto userRequestDto) {
        // 일정 존재 여부 확인 및 객체 생성
        Schedule schedule = getSchedule(scheduleId);

        // 사용자 권한체크
        checkUserAuthority(userRequestDto, schedule);

        // 일정에 연동된 댓글 삭제 (소프트 삭제)
        schedule.deleteComment();

        // 일정 삭제 (소프트 삭제)
        schedule.deletedTime();


        return scheduleId + "번 일정이 삭제 되었습니다.";
    }


    /**
     * 로그인해서 요청을한 유저의 권한 확인
     * @param userRequestDto 로그인한 유저의 정보
     * @param schedule 일정 객체
     */
    private void checkUserAuthority(UserRequestDto userRequestDto, Schedule schedule) {
        if (userRequestDto.getRole() != UserRoleEnum.ADMIN) {
            schedule.getUser().checkAuthority(userRequestDto.getUsername());
        }
    }

    /**
     * 유저 객체 생성
     * @param username 유저이름 (유니크)
     * @return 유저 객체
     */
    private User getUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new NotFoundException("해당하는 유저가 없습니다.")
        );
    }

    // 일정 찾고 유효성 확인
    private Schedule getSchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findByScheduleIdAndScheduleDeleteAtIsNull(scheduleId);
        if (schedule == null) {
            throw new IllegalArgumentException("해당하는 일정은 없거나 삭제되었습니다.");
        }
        return schedule;
    }
}
