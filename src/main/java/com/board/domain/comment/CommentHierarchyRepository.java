package com.board.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentHierarchyRepository extends JpaRepository<CommentHierarchy, Long> {
}
