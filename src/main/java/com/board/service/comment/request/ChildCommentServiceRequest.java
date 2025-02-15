package com.board.service.comment.request;

import com.board.domain.article.Article;
import com.board.domain.comment.Comment;
import com.board.domain.member.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChildCommentServiceRequest {

    private Long parentCommentId;
    private String content;

    @Builder(access = AccessLevel.PRIVATE)
    public ChildCommentServiceRequest(Long parentCommentId, String content) {
        this.parentCommentId = parentCommentId;
        this.content = content;
    }

    public static ChildCommentServiceRequest of(Long parentCommentId, String content) {
        return ChildCommentServiceRequest.builder()
                .parentCommentId(parentCommentId)
                .content(content)
                .build();
    }

    public Comment toEntity(Article article, Member member) {
        return Comment.builder()
                .article(article)
                .member(member)
                .content(content)
                .deleted(false)
                .build();
    }

}
