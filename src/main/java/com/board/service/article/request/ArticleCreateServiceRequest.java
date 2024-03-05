package com.board.service.article.request;

import com.board.domain.article.Article;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ArticleCreateServiceRequest {

    private String title;
    private String content;

    @Builder
    private ArticleCreateServiceRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Article toEntity() {
        return Article.builder()
                .title(title)
                .content(content)
                .deleted(false)
                .build();
    }

}
