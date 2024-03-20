package com.board.service.article.response;

import com.board.domain.article.Article;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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
