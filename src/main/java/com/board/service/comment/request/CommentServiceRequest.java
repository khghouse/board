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
public class CommentServiceRequest {

    private Long id;
    private String content;
    private Long articleId;

    @Builder(access = AccessLevel.PRIVATE)
    private CommentServiceRequest(Long id, String content, Long articleId) {
        this.id = id;
        this.content = content;
        this.articleId = articleId;
    }

    public static CommentServiceRequest of(String content, Long articleId) {
        return CommentServiceRequest.builder()
                .content(content)
                .articleId(articleId)
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
