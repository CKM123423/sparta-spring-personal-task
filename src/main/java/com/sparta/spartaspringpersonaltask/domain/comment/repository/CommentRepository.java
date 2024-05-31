package com.sparta.spartaspringpersonaltask.domain.comment.repository;

import com.sparta.spartaspringpersonaltask.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByCommentIdAndCommentDeleteAtIsNull(Long commentId);

}