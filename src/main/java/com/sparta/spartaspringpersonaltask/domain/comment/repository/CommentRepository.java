package com.sparta.spartaspringpersonaltask.domain.comment.repository;

import com.sparta.spartaspringpersonaltask.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 이것을 쓰는게 효율적일지 아니면 id로 찾은다음 필터로 널체크를 하는게 효율적일지
    // 가시성은 개인적으로 필터쪽이 편하다고 느끼나 효율은 어느쪽이 우세한지 잘모르겠음
    Optional<Comment> findByCommentIdAndCommentDeleteAtIsNull(Long commentId);

    List<Comment> findBySchedule_ScheduleId(Long scheduleId);
}
