package com.sparta.spartaspringpersonaltask.service;

import com.sparta.spartaspringpersonaltask.dto.ScheduleRequestDto;
import com.sparta.spartaspringpersonaltask.dto.ScheduleResponseDto;
import com.sparta.spartaspringpersonaltask.entity.Schedule;
import com.sparta.spartaspringpersonaltask.exceptions.customexceptions.NotFoundException;
import com.sparta.spartaspringpersonaltask.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        Schedule schedule = scheduleRequestDto.toEntity();

        // DB 저장
        scheduleRepository.save(schedule);

        // Entity -> DTO
        return new ScheduleResponseDto(schedule);
    }

    // 단일 일정 조회
    public ScheduleResponseDto viewSelectedSchedule(Long scheduleKey) {
        // 일정 존재 여부 확인 및 객체 정보 저장
        Schedule schedule = findSchedule(scheduleKey);

        // 삭제 여부 확인
        schedule.checkDeletionStatus();

        // 조회
        return new ScheduleResponseDto(schedule);
    }

    // 전체 일정 조회
    public List<ScheduleResponseDto> viewAllSchedules() {
        return scheduleRepository.findAllByOrderByScheduleDatetimeDesc().stream()
                .filter(schedule -> !schedule.isDeletionStatus())
                .map(ScheduleResponseDto::new)
                .toList();
    }

    // 일정 수정
    @Transactional
    public ScheduleResponseDto modifySchedule(Long scheduleKey, ScheduleRequestDto requestDto) {
        // 일정 존재 여부 확인 및 객체 정보 저장
        Schedule schedule = findSchedule(scheduleKey);

        // 삭제 여부 확인
        schedule.checkDeletionStatus();

        // 비밀번호 확인
        schedule.checkPassword(requestDto.getSchedulePassword());

        // 수정 내용 저장
        schedule.update(requestDto);

        // 수정 내용 조회
        return new ScheduleResponseDto(schedule);
    }

    // 일정 삭제 기능
    public Long deleteSchedule(Long scheduleKey, ScheduleRequestDto requestDto) {
        // 일정 존재 여부 확인 및 객체 정보 저장
        Schedule schedule = findSchedule(scheduleKey);

        // 삭제 여부 확인
        schedule.checkDeletionStatus();

        // 비밀번호 확인
        schedule.checkPassword(requestDto.getSchedulePassword());

        // 일정 삭제 (소프트 삭제)
        schedule.markAsDeleted();

        // DB에 저장
        scheduleRepository.save(schedule);

        return scheduleKey;
    }

    // DB 에서 일정을 찾아 반환
    private Schedule findSchedule(Long scheduleKey) {
        return scheduleRepository.findById(scheduleKey)
                .orElseThrow(() -> new NotFoundException("선택한 일정이 없습니다."));
    }
}
