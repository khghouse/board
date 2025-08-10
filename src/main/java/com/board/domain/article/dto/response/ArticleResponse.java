package com.board.domain.article.dto.response;

import com.board.domain.article.entity.Article;

public record ArticleResponse(
        Long id,
        String title,
        String content
) {
    public static ArticleResponse of(Article article) {
        return new ArticleResponse(
                article.getId(),
                article.getTitle(),
                article.getContent()
        );
    }
}
