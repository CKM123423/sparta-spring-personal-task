package com.sparta.spartaspringpersonaltask.domain.comment.service;

import com.sparta.spartaspringpersonaltask.domain.comment.entity.Comment;
import com.sparta.spartaspringpersonaltask.domain.comment.repository.CommentRepository;
import com.sparta.spartaspringpersonaltask.domain.schedule.entity.Schedule;
import com.sparta.spartaspringpersonaltask.domain.schedule.repository.ScheduleRepository;
import com.sparta.spartaspringpersonaltask.global.dto.comment.CommentRequestDto;
import com.sparta.spartaspringpersonaltask.global.dto.comment.CommentResponseDto;
import com.sparta.spartaspringpersonaltask.global.exceptions.customexceptions.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;

    public CommentService(CommentRepository commentRepository, ScheduleRepository scheduleRepository) {
        this.commentRepository = commentRepository;
        this.scheduleRepository = scheduleRepository;
    }

    /**
     * 댓글 등록 기능
     *
     * @param id 댓글을 작성할 스케줄의 고유번호
     * @param requestDto 댓글내용, 사용자이름
     * @return 댓글고유번호, 스케줄 고유번호, 댓글작성자, 댓글내용, 댓글작성시간
     *
     */
    public CommentResponseDto createComment(Long scheduleKey, CommentRequestDto requestDto) {
        Schedule schedule = scheduleRepository.findById(scheduleKey).orElseThrow(
                () -> new NotFoundException("선택한 일정이 없습니다.")
        );

        Comment comment = toEntity(schedule, requestDto);

        commentRepository.save(comment);

        return toDto(comment);
    }


    /**
     *  dto -> entity
     *
     * @param schedule 스케줄 entity
     * @param requestDto 댓글내용, 사용자이름
     * @return 댓글고유번호, 스케줄 고유번호, 댓글작성자, 댓글내용, 댓글작성시간
     */
    private Comment toEntity(Schedule schedule, CommentRequestDto requestDto) {
        return Comment.builder()
                .schedule(schedule)
                .commentContent(requestDto.getCommentContent())
                .commentUserName(requestDto.getUserName())
                .build();
    }

    /**
     * entity -> dto
     *
     * @param comment 댓글고유번호, 스케줄 고유번호, 댓글작성자, 댓글내용, 댓글작성시간
     * @return 댓글고유번호, 스케줄 고유번호, 댓글작성자, 댓글내용, 댓글작성시간
     */
    private CommentResponseDto toDto(Comment comment) {
        return new CommentResponseDto(comment);
    }
}
