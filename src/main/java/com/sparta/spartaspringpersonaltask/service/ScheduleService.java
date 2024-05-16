package com.sparta.spartaspringpersonaltask.service;

import com.sparta.spartaspringpersonaltask.dto.ScheduleRequestDto;
import com.sparta.spartaspringpersonaltask.dto.ScheduleResponseDto;
import com.sparta.spartaspringpersonaltask.entity.Schedule;
import com.sparta.spartaspringpersonaltask.exceptions.InvalidPasswordException;
import com.sparta.spartaspringpersonaltask.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    // 일정 등록
    public ScheduleResponseDto createSchedule(ScheduleRequestDto scheduleRequestDto) {
        // DTO -> Entity
        Schedule schedule = new Schedule(scheduleRequestDto);

        // DB 저장
        scheduleRepository.save(schedule);

        // Entity -> DTO
        return new ScheduleResponseDto(schedule);
    }

    // 단일 일정 조회
    public ScheduleResponseDto viewSelectedSchedule(Long scheduleKey) {
        // 일정 존재 여부 확인
        Schedule schedule = findSchedule(scheduleKey);

        // 조회
        return new ScheduleResponseDto(schedule);
    }

    // 전체 일정 조회
    public List<ScheduleResponseDto> viewAllSchedules() {
        return scheduleRepository
                .findAll()
                .stream()
                .map(ScheduleResponseDto::new)
                .toList();
    }

    // 일정 수정
    @Transactional
    public ScheduleResponseDto modifySchedule(Long scheduleKey, ScheduleRequestDto requestDto) {
        // 일정 존재 여부 확인
        Schedule schedule = findSchedule(scheduleKey);

        // 비밀번호 추출
        String DtoPassword = changePasswordToString(requestDto);

        // 비밀번호 확인
        checkPassword(scheduleKey, DtoPassword);

        // 수정 내용 저장
        schedule.update(requestDto);

        // 수정 내용 조회
        return new ScheduleResponseDto(schedule);
    }

    // 일정 삭제 기능
    public Long deleteSchedule(Long scheduleKey, String password) {
        // 일정 존재 여부 확인
        Schedule schedule = findSchedule(scheduleKey);

        // 비밀번호 확인
        checkPassword(scheduleKey, password);

        // 일정 삭제
        scheduleRepository.delete(schedule);

        return scheduleKey;
    }

    // DB에 일정 조회
    private Schedule findSchedule(Long scheduleKey) {
        return scheduleRepository
                .findById(scheduleKey)
                .orElseThrow(() -> new IllegalArgumentException("선택한 일정이 없습니다."));
    }

    // 비밀번호 확인
    private void checkPassword(Long scheduleKey, String password) {

        // DB에 저장된 비밀번호
        String storedPassword = scheduleRepository.findById(scheduleKey)
                .map(Schedule::getSchedulePassword)
                .orElse(null);

        // 비밀번호가 틀릴경우 예외 발생
        if (!Objects.equals(password, storedPassword)) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
        }
    }

    // DTO 에 저장된 비밀번호 추출
    private String changePasswordToString(ScheduleRequestDto requestDto) {
        return requestDto.getSchedulePassword();
    }
}
