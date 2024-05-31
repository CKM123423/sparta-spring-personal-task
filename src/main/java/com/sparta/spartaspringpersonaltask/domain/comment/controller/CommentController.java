package com.sparta.spartaspringpersonaltask.domain.comment.controller;

import com.sparta.spartaspringpersonaltask.domain.comment.service.CommentService;
import com.sparta.spartaspringpersonaltask.global.dto.comment.CommentRequestDto;
import com.sparta.spartaspringpersonaltask.global.dto.comment.CommentResponseDto;
import com.sparta.spartaspringpersonaltask.global.auth.security.UserDetailsImpl;
import com.sparta.spartaspringpersonaltask.global.dto.user.UserRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * 댓글 등록 기능
     * @param scheduleId 스케줄 고유번호
     * @param requestDto 댓글내용
     * @param userDetails 유저정보
     * @return 댓글고유번호, 스케줄 고유번호, 댓글작성자, 댓글내용, 댓글작성시간
     */
    @PostMapping("/create/{scheduleId}")
    public CommentResponseDto createComment(@PathVariable Long scheduleId,
                                            @Valid @RequestBody CommentRequestDto requestDto,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();
        return commentService.createComment(scheduleId, requestDto, username);
    }

    /**
     * 댓글 수정 기능
     * @param commentId 댓글 고유번호
     * @param requestDto 댓글 수정내용
     * @param userDetails 유저정보
     * @return 댓글고유번호, 스케줄 고유번호, 댓글작성자, 수정된 댓글내용, 댓글작성시간
     */
    @PutMapping("/update/{commentId}")
    public CommentResponseDto updateComment(@PathVariable Long commentId,
                                            @Valid @RequestBody CommentRequestDto requestDto,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateComment(commentId, requestDto, userInfo(userDetails));
    }

    /**
     * 댓글 삭제 기능
     * @param commentId 댓글 고유번호
     * @param userDetails 유저 정보
     * @return 댓글 삭제 메세지, http status
     */
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{commentId}")
    public String deleteComment(@PathVariable Long commentId,
                                @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.deleteComment(commentId, userInfo(userDetails));
    }


    /**
     * 유저의 권한을 확인하기 위한 정보전달 객체
     *
     * @param userDetails 유저의 정보
     * @return 유저의 정보전달 객체
     */
    private UserRequestDto userInfo(UserDetailsImpl userDetails) {
        return new UserRequestDto(userDetails.getUser().getUsername(), userDetails.getUser().getRole());
    }
}
