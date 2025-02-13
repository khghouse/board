package com.board.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentHierarchyRepository extends JpaRepository<CommentHierarchy, Long> {

    List<CommentHierarchy> findAllByDescendant(Comment comment);

}
