package com.board.domain.article.dto.response;

import com.board.domain.article.entity.Article;
import com.board.domain.member.dto.response.MemberResponse;
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
