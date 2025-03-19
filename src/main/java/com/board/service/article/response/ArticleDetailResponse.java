package com.board.service.article.response;

import com.board.domain.article.Article;
import com.board.service.member.response.MemberResponse;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ArticleDetailResponse(
        Long id,
        String title,
        String content,
        int viewCount,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdDateTime,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime modifiedDateTime,
        MemberResponse member
) {
    public static ArticleDetailResponse of(Article article) {
        return new ArticleDetailResponse(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getViewCount(),
                article.getCreatedDateTime(),
                article.getModifiedDateTime(),
                MemberResponse.of(article.getMember())
        );
    }
}
