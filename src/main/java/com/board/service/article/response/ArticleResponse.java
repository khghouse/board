package com.board.service.article.response;

import com.board.domain.article.Article;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ArticleResponse {

    private final Long id;
    private final String title;
    private final String content;

    @JsonIgnore
    private final Boolean deleted;

    @JsonIgnore
    private final LocalDateTime createdDateTime;

    @JsonIgnore
    private final LocalDateTime modifiedDateTime;

    @Builder
    private ArticleResponse(Long id, String title, String content, Boolean deleted, LocalDateTime createdDateTime, LocalDateTime modifiedDateTime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.deleted = deleted;
        this.createdDateTime = createdDateTime;
        this.modifiedDateTime = modifiedDateTime;
    }

    public static ArticleResponse of(Article article) {
        return ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .deleted(article.getDeleted())
                .createdDateTime(article.getCreatedDateTime())
                .modifiedDateTime(article.getModifiedDateTime())
                .build();
    }

}
