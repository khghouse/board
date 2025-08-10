package com.board.domain.comment.service;

import com.board.domain.comment.entity.Comment;
import com.board.domain.comment.entity.CommentHierarchy;
import com.board.domain.comment.repository.CommentHierarchyRepository;
import com.board.domain.comment.dto.request.CommentHierarchyServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentHierarchySerivce {

    private final CommentHierarchyRepository commentHierarchyRepository;

    @Transactional
    public void createCommentHierarchy(CommentHierarchyServiceRequest request) {
        Comment comment = request.getComment();

        // 본 댓글에 대한 계층 데이터 등록
        commentHierarchyRepository.save(request.toEntity(comment, 0));

        // 부모 댓글이 존재한다면 이번 댓글이 대댓글임을 의미한다.
        if (null != request.getParentComment()) {
            // 부모 댓글의 모든 조상 댓글 조회
            List<CommentHierarchy> ancestorHierarchies = commentHierarchyRepository.findAllByDescendant(request.getParentComment());

            List<CommentHierarchy> newHierarchies = new ArrayList<>();
            for (CommentHierarchy commentHierarchy : ancestorHierarchies) {
                newHierarchies.add(CommentHierarchy.of(commentHierarchy.getAncestor(), comment, commentHierarchy.getDepth() + 1));
            }

            commentHierarchyRepository.saveAll(newHierarchies);
        }
    }

}
