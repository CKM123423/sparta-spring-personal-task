package com.sparta.spartaspringpersonaltask.domain.comment.controller;

import com.sparta.spartaspringpersonaltask.domain.comment.service.CommentService;
import com.sparta.spartaspringpersonaltask.global.dto.comment.CommentRequestDto;
import com.sparta.spartaspringpersonaltask.global.dto.comment.CommentResponseDto;
import com.sparta.spartaspringpersonaltask.global.security.UserDetailsImpl;
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
     * @param scheduleKey 스케줄 고유번호
     * @param requestDto 댓글내용
     * @param userDetails 유저정보
     * @return 댓글고유번호, 스케줄 고유번호, 댓글작성자, 댓글내용, 댓글작성시간
     */
    @PostMapping("/create/{scheduleKey}")
    public CommentResponseDto createComment(@PathVariable Long scheduleKey,
                                            @Valid @RequestBody CommentRequestDto requestDto,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String userName = userDetails.getUsername();
        return commentService.createComment(scheduleKey, requestDto, userName);
    }

    /**
     * 댓글 수정 기능
     * @param commentKey 댓글 고유번호
     * @param requestDto 댓글 수정내용
     * @param userDetails 유저정보
     * @return 댓글고유번호, 스케줄 고유번호, 댓글작성자, 수정된 댓글내용, 댓글작성시간
     */
    @PutMapping("/update/{commentKey}")
    public CommentResponseDto updateComment(@PathVariable Long commentKey,
                                            @Valid @RequestBody CommentRequestDto requestDto,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String userName = userDetails.getUsername();
        return commentService.updateComment(commentKey, requestDto, userName);
    }

    /**
     * 댓글 삭제 기능
     * @param commentKey 댓글 고유번호
     * @param userDetails 유저 정보
     * @return 댓글 삭제 메세지, http status
     */
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{commentKey}")
    public String deleteComment(@PathVariable Long commentKey,
                                @AuthenticationPrincipal UserDetailsImpl userDetails){
        String userName = userDetails.getUsername();
        return commentService.deleteComment(commentKey, userName);
    }
}
