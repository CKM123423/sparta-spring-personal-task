package com.sparta.spartaspringpersonaltask.domain.comment.repository;

import com.sparta.spartaspringpersonaltask.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
