package com.board.domain.comment.repository;

import com.board.domain.comment.entity.Comment;
import com.board.domain.comment.entity.CommentHierarchy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentHierarchyRepository extends JpaRepository<CommentHierarchy, Long> {

    List<CommentHierarchy> findAllByDescendant(Comment comment);

}
