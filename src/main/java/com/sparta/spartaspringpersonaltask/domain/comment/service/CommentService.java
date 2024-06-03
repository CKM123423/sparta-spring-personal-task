package com.sparta.spartaspringpersonaltask.domain.comment.service;

import com.sparta.spartaspringpersonaltask.domain.comment.entity.Comment;
import com.sparta.spartaspringpersonaltask.domain.comment.repository.CommentRepository;
import com.sparta.spartaspringpersonaltask.domain.schedule.entity.Schedule;
import com.sparta.spartaspringpersonaltask.domain.schedule.repository.ScheduleRepository;
import com.sparta.spartaspringpersonaltask.domain.user.entity.User;
import com.sparta.spartaspringpersonaltask.domain.user.entity.UserRoleEnum;
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
     * @param userId   유저 ID
     * @param scheduleId 댓글을 작성할 스케줄의 고유번호
     * @param requestDto 댓글내용
     * @return 댓글고유번호, 스케줄 고유번호, 댓글작성자, 댓글내용, 댓글작성시간
     */
    @Transactional
    public CommentResponseDto createComment(Long scheduleId, CommentRequestDto requestDto, Long userId) {

        Schedule schedule = scheduleRepository.findByScheduleIdAndScheduleDeleteAtIsNull(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 일정을 찾을 수 없거나 삭제되었습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() ->  new NotFoundException("해당하는 유저를 찾을 수 없습니다."));
        
        Comment comment = requestDto.toEntity(schedule, user);

        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    // 지금은 어드민권한에 단순한 수정삭제 권한을 주느라 유저객체를 만들었지만
    // 원래라면 어드민은 더욱 많은 권한에 따로 뺴는경우가 많아
    // 사용자 비교 자체는 id 자체로만 하면 유저객체를 만들지 않아도된다

    // 다만 그경우 본인을 확인하는 기능자체가 서비스나 엔티티에 중복이될경우가 발생한다.
    // 해당 프로젝트에서는 일정과 댓글에서 중복이 일어날 수 있음

    // 결국 어디에 초점을 둘것이냐가 관점일듯

    // 예전코드가 아래의 코드

    // User user = getUserByUserId(userId);
    // user.checkAuthorityByUserId(comment.getUser().getUserId());
    /**
     * 댓글 수정 기능
     *
     * @param commentId  댓글 고유번호
     * @param requestDto 댓글내용
     * @param userId     유저 아이디
     * @param role 로그인 유저 권한
     * @return 댓글고유번호, 스케줄 고유번호, 댓글작성자, 수정된 댓글내용, 댓글작성시간
     */
    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto, Long userId, UserRoleEnum role) {
        Comment comment = getCommentByCommentId(commentId);

        if (role != UserRoleEnum.ADMIN) {
            comment.checkUser(userId);
        }

        comment.updateContent(requestDto.getCommentContent());

        return new CommentResponseDto(comment);
    }

    /**
     * 댓글 삭제 기능
     *
     * @param commentId 댓글 고유번호
     * @param userId    유저 아이디
     * @param role 로그인 유저 권한
     * @return 댓글 삭제 메세지
     */
    @Transactional
    public String deleteComment(Long commentId, Long userId, UserRoleEnum role) {
        Comment comment = getCommentByCommentId(commentId);

        if (role != UserRoleEnum.ADMIN) {
            comment.checkUser(userId);
        }

        comment.deletedTime();

        return commentId + "번 댓글이 삭제 되었습니다.";
    }

    /**
     * 댓글 객체 생성 및 검증
     * @param commentId 댓글 고유 id
     * @return 댓글 객체
     */
    // 데이터에 부담을 안주는쪽으로 생각을 하자. 대신 쿼리문이 많아졌을때는 그걸 다 필터를 돌려야하니 그점만 유의
    private Comment getCommentByCommentId(Long commentId) {
        return commentRepository.findById(commentId)
                .filter(comment -> comment.getCommentDeleteAt() == null)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 댓글은 없거나 삭제되었습니다."));

    }
}
