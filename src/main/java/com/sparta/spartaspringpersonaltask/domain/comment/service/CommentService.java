package com.sparta.spartaspringpersonaltask.domain.comment.service;

import com.sparta.spartaspringpersonaltask.domain.comment.entity.Comment;
import com.sparta.spartaspringpersonaltask.domain.comment.repository.CommentRepository;
import com.sparta.spartaspringpersonaltask.domain.schedule.entity.Schedule;
import com.sparta.spartaspringpersonaltask.domain.schedule.repository.ScheduleRepository;
import com.sparta.spartaspringpersonaltask.domain.user.entity.User;
import com.sparta.spartaspringpersonaltask.domain.user.repository.UserRepository;
import com.sparta.spartaspringpersonaltask.global.dto.comment.CommentRequestDto;
import com.sparta.spartaspringpersonaltask.global.dto.comment.CommentResponseDto;
import com.sparta.spartaspringpersonaltask.global.exception.customexceptions.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository,
                          ScheduleRepository scheduleRepository,
                          UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
    }

    // 유저서비스내에 스케줄이있고
    // 스케줄내에 유저서비스
    /**
     * 댓글 등록 기능
     *
     * @param scheduleKey 댓글을 작성할 스케줄의 고유번호
     * @param requestDto  댓글내용
     * @param userName 유저 ID
     * @return 댓글고유번호, 스케줄 고유번호, 댓글작성자, 댓글내용, 댓글작성시간
     */
    public CommentResponseDto createComment(Long scheduleKey, CommentRequestDto requestDto, String userName) {
        Schedule schedule = scheduleRepository.findById(scheduleKey).orElseThrow(
                () -> new NotFoundException("선택한 일정이 없습니다.")
        );
        schedule.checkDeletionStatus();

        User user = userRepository.findByUserName(userName).orElseThrow(
                () -> new NotFoundException("해당하는 유저가 없습니다.")
        );

        Comment comment = toEntity(schedule, user, requestDto);

        commentRepository.save(comment);

        return toDto(comment);
    }

    /**
     * 댓글 수정 기능
     *
     * @param commentKey 댓글 고유번호
     * @param requestDto 댓글내용
     * @param userName 유저 ID
     * @return 댓글고유번호, 스케줄 고유번호, 댓글작성자, 수정된 댓글내용, 댓글작성시간
     */
    @Transactional
    public CommentResponseDto updateComment(Long commentKey, CommentRequestDto requestDto, String userName) {
        Comment comment = retrieveComment(commentKey);

        User user =  userRepository.findByUserName(userName).orElseThrow(
                () -> new NotFoundException("해당하는 유저가 없습니다.")
        );

        Comment commentToUpdate = toEntity(comment.getSchedule(), user,requestDto);

        comment.checkUser(user);

        comment.update(commentToUpdate);

        return toDto(comment);
    }

    /**
     * 댓글 삭제 기능
     * @param commentKey 댓글 고유번호
     * @param userName 유저 ID
     * @return 댓글 삭제 메세지
     */
    @Transactional
    public String deleteComment(Long commentKey, String userName) {
        Comment comment = retrieveComment(commentKey);

        User user =  userRepository.findByUserName(userName).orElseThrow(
                () -> new NotFoundException("해당하는 유저가 없습니다.")
        );

        comment.checkUser(user);

        comment.deletedTime();

        return commentKey + "번 댓글이 삭제 되었습니다.";
    }

    /**
     * 댓글 유효성 확인
     * @param commentId 댓글 고유번호
     * @return 댓글고유번호, 스케줄, 유저정보, 댓글내용, 댓글작성시간
     */
    private Comment retrieveComment(Long commentId) {
        Comment comment=  commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("선택한 댓글이 없습니다.")
        );

        comment.checkDeletionStatus();

        return comment;
    }

    /**
     *  dto -> entity
     * @param schedule 스케줄 entity
     * @param requestDto 댓글내용, 사용자이름
     * @return 댓글고유번호, 스케줄, 댓글작성자, 댓글내용, 댓글작성시간
     */
    private Comment toEntity(Schedule schedule, User user, CommentRequestDto requestDto) {
        return Comment.builder()
                .schedule(schedule)
                .user(user)
                .commentContent(requestDto.getCommentContent())
                .build();
    }

    /**
     * entity -> dto
     * @param comment 댓글고유번호, 스케줄, 댓글작성자, 댓글내용, 댓글작성시간
     * @return 댓글고유번호, 스케줄 고유번호, 댓글작성자, 댓글내용, 댓글작성시간
     */
    private CommentResponseDto toDto(Comment comment) {
        return new CommentResponseDto(comment);
    }
}
