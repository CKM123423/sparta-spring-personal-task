package com.sparta.spartaspringpersonaltask.service;

import com.sparta.spartaspringpersonaltask.dto.ScheduleRequestDto;
import com.sparta.spartaspringpersonaltask.dto.ScheduleResponseDto;
import com.sparta.spartaspringpersonaltask.entity.Schedule;
import com.sparta.spartaspringpersonaltask.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public ScheduleResponseDto createSchedule(ScheduleRequestDto scheduleRequestDto) {
        // DTO -> Entity
        Schedule schedule = new Schedule(scheduleRequestDto);

        // DB 저장
        scheduleRepository.save(schedule);

        // Entity -> DTO
        return new ScheduleResponseDto(schedule);
    }

    public ScheduleResponseDto viewSelectedSchedule(Long scheduleKey) {
        // 일정 존재 여부 확인
        Schedule schedule = findSchedule(scheduleKey);

        // 조회
        return new ScheduleResponseDto(schedule);
    }

    private Schedule findSchedule(Long scheduleKey) {
        return scheduleRepository
                .findById(scheduleKey)
                .orElseThrow(() -> new IllegalArgumentException("선택한 일정이 없습니다."));
    }
}
