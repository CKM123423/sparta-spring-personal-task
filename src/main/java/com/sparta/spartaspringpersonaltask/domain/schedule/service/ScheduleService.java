package com.sparta.spartaspringpersonaltask.domain.schedule.service;

import com.sparta.spartaspringpersonaltask.domain.comment.entity.Comment;
import com.sparta.spartaspringpersonaltask.global.dto.schedule.ScheduleDeleteRequestDto;
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

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    // 일정 등록
    public ScheduleResponseDto createSchedule(ScheduleRequestDto requestDto) {
        // DTO -> Entity
        Schedule schedule = toEntity(requestDto);

        // DB 저장
        scheduleRepository.save(schedule);

        // Entity -> DTO
        return toDto(schedule);
    }

    // 단일 일정 조회
    public ScheduleResponseDto viewSelectedSchedule(Long scheduleKey) {
        // 일정 존재 여부 확인 및 객체 생성
        Schedule schedule = findSchedule(scheduleKey);

        // 삭제 여부 확인
        schedule.checkDeletionStatus();

        // 조회
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
    public ScheduleResponseDto modifySchedule(Long scheduleKey, ScheduleRequestDto requestDto) {
        // 일정 존재 여부 확인 및 객체 생성
        Schedule schedule = findSchedule(scheduleKey);
        Schedule scheduleToUpdate = toEntity(requestDto);

        // 삭제 여부 확인
        schedule.checkDeletionStatus();

        // 비밀번호 확인
        schedule.checkPassword(scheduleToUpdate.getSchedulePassword());

        // 수정 내용 저장
        schedule.update(scheduleToUpdate);

        // 수정 내용 조회
        return toDto(schedule);
    }

    // 일정 삭제 기능
    @Transactional
    public String deleteSchedule(Long scheduleKey, ScheduleDeleteRequestDto requestDto) {
        // 일정 존재 여부 확인 및 객체 생성
        String inputPassword = requestDto.getSchedulePassword();
        Schedule schedule = findSchedule(scheduleKey);

        // 삭제 여부 확인
        schedule.checkDeletionStatus();

        // 비밀번호 확인
        schedule.checkPassword(inputPassword);

        // 일정 삭제 (소프트 삭제)
        schedule.deletedTime();

        // 일정에 연동된 댓글 삭제
        List<Comment> deleteComment = schedule.getCommentList();

        for (Comment comment : deleteComment) {
            comment.deletedTime();
        }

        return scheduleKey + "번 일정이 삭제 되었습니다.";
    }

    // DB 에서 일정을 찾아 반환
    private Schedule findSchedule(Long scheduleKey) {
        return scheduleRepository.findById(scheduleKey)
                .orElseThrow(() -> new NotFoundException("선택한 일정이 없습니다."));
    }


    private Schedule toEntity(ScheduleRequestDto requestDto) {
        return Schedule.builder()
                .scheduleTitle(requestDto.getScheduleTitle())
                .scheduleContent(requestDto.getScheduleContent())
                .scheduleManager(requestDto.getScheduleManager())
                .schedulePassword(requestDto.getSchedulePassword())
                .build();
    }

    private ScheduleResponseDto toDto(Schedule schedule) {
        return new ScheduleResponseDto(schedule);
    }
}
