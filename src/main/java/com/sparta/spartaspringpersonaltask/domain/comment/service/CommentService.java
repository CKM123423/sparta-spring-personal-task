package com.sparta.spartaspringpersonaltask.domain.comment.service;

import com.sparta.spartaspringpersonaltask.domain.comment.entity.Comment;
import com.sparta.spartaspringpersonaltask.domain.comment.repository.CommentRepository;
import com.sparta.spartaspringpersonaltask.domain.schedule.entity.Schedule;
import com.sparta.spartaspringpersonaltask.domain.schedule.service.ScheduleService;
import com.sparta.spartaspringpersonaltask.domain.user.entity.User;
import com.sparta.spartaspringpersonaltask.domain.user.service.UserService;
import com.sparta.spartaspringpersonaltask.global.dto.comment.CommentRequestDto;
import com.sparta.spartaspringpersonaltask.global.dto.comment.CommentResponseDto;
import com.sparta.spartaspringpersonaltask.global.exception.customexceptions.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ScheduleService scheduleService;
    private final UserService userService;

    public CommentService(CommentRepository commentRepository,
                          ScheduleService scheduleService,
                          UserService userService) {
        this.commentRepository = commentRepository;
        this.scheduleService = scheduleService;
        this.userService = userService;
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
    @Transactional
    public CommentResponseDto createComment(Long scheduleKey, CommentRequestDto requestDto, String userName) {
        Schedule schedule = scheduleService.retrieveSchedule(scheduleKey);

        User user = userService.getUserByUsername(userName);

        Comment comment = requestDto.toEntity(schedule, user);

        commentRepository.save(comment);

        return new CommentResponseDto(comment);
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

        User user =  userService.getUserByUsername(userName);
        validateUserPermission(user, comment);

        Comment commentToUpdate = requestDto.toEntity(comment.getSchedule(), user);

        comment.update(commentToUpdate);

        return new CommentResponseDto(comment);
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

        User user =  userService.getUserByUsername(userName);
        validateUserPermission(user, comment);

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

        comment.getSchedule().checkDeletionStatus();

        comment.checkDeletionStatus();

        return comment;
    }

    /**
     * 접근 유효성 확인
     * @param user 권한을 확인하는 유저 객체
     * @param comment 사용자를 확인하기 위한 객체
     */
    private void validateUserPermission(User user, Comment comment) {
        if (!user.isAdmin()) {
            user.checkUser(comment.getUser());
        }
    }
}
