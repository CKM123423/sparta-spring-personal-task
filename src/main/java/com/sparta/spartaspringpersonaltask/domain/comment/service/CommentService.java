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

    /**
     * 댓글 등록 기능
     *
     * @param scheduleId 댓글을 작성할 스케줄의 고유번호
     * @param requestDto  댓글내용
     * @param username 유저 ID
     * @return 댓글고유번호, 스케줄 고유번호, 댓글작성자, 댓글내용, 댓글작성시간
     */
    @Transactional
    public CommentResponseDto createComment(Long scheduleId, CommentRequestDto requestDto, String username) {

        Schedule schedule = scheduleRepository.findByScheduleIdAndScheduleDeleteAtIsNull(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 일정을 찾을 수 없거나 삭제되었습니다."));

        User user = getUserByUsername(username);
        
        Comment comment = requestDto.toEntity(schedule, user);

        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    /**
     * 댓글 수정 기능
     *
     * @param commentId 댓글 고유번호
     * @param requestDto 댓글내용
     * @param username 유저 정보
     * @return 댓글고유번호, 스케줄 고유번호, 댓글작성자, 수정된 댓글내용, 댓글작성시간
     */
    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto, String username) {
        Comment comment = getComment(commentId);

        User user = getUserByUsername(username);

        user.checkAuthority(comment.getUser().getUserId());

        comment.update(requestDto.getCommentContent());

        return new CommentResponseDto(comment);
    }

    /**
     * 댓글 삭제 기능
     * @param commentId 댓글 고유번호
     * @param username 유저 정보
     * @return 댓글 삭제 메세지
     */
    @Transactional
    public String deleteComment(Long commentId, String username) {
        Comment comment = getComment(commentId);

        User user = getUserByUsername(username);

        user.checkAuthority(comment.getUser().getUserId());

        comment.deletedTime();

        return commentId + "번 댓글이 삭제 되었습니다.";
    }

    /**
     * 댓글 객체 생성 및 검증
     * @param commentId 댓글 고유 id
     * @return 댓글 객체
     */
    private Comment getComment(Long commentId) {
        return commentRepository.findByCommentIdAndCommentDeleteAtIsNull(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 댓글은 없거나 삭제되었습니다."));
    }

    /**
     * 유저이름으로 유저 객체 생성
     * @param username 유저 이름
     * @return 유저 객체
     */
    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("해당하는 유저를 찾을 수 없습니다."));
    }
}
