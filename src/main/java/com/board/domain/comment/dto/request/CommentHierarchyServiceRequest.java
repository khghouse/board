package com.board.domain.comment.dto.request;

import com.board.domain.comment.entity.Comment;
import com.board.domain.comment.entity.CommentHierarchy;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentHierarchyServiceRequest {

    private Comment comment;
    private Comment parentComment;

    @Builder(access = AccessLevel.PRIVATE)
    private CommentHierarchyServiceRequest(Comment comment, Comment parentComment) {
        this.comment = comment;
        this.parentComment = parentComment;
    }

    public static CommentHierarchyServiceRequest of(Comment comment, Comment parentComment) {
        return CommentHierarchyServiceRequest.builder()
                .comment(comment)
                .parentComment(parentComment)
                .build();
    }

    public static CommentHierarchyServiceRequest withComment(Comment comment) {
        return CommentHierarchyServiceRequest.builder()
                .comment(comment)
                .build();
    }

    public CommentHierarchy toEntity(Comment ancestorComment, int depth) {
        return CommentHierarchy.builder()
                .ancestor(ancestorComment)
                .descendant(comment)
                .depth(depth)
                .build();
    }

}
