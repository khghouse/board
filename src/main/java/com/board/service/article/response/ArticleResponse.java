package com.board.service.article.response;

import com.board.domain.article.Article;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ArticleResponse {

    private String title;
    private String content;
    private LocalDateTime createdDateTime;
    private LocalDateTime modifiedDateTime;

    @Builder
    private ArticleResponse(String title, String content, LocalDateTime createdDateTime, LocalDateTime modifiedDateTime) {
        this.title = title;
        this.content = content;
        this.createdDateTime = createdDateTime;
        this.modifiedDateTime = modifiedDateTime;
    }

    public static ArticleResponse of(Article article) {
        return ArticleResponse.builder()
                .title(article.getTitle())
                .content(article.getContent())
                .createdDateTime(article.getCreatedDateTime())
                .modifiedDateTime(article.getModifiedDateTime())
                .build();
    }

}
