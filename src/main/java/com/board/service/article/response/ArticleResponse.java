package com.board.service.article.response;

import com.board.domain.article.Article;

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
