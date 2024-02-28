package com.board.service.article.request;

import com.board.domain.article.Article;
import jakarta.validation.constraints.Max;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ArticleCreateServiceRequest {

    @Max(1)
    private String title;
    private String content;

    @Builder
    public ArticleCreateServiceRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Article toEntity() {
        return Article.builder()
                .title(title)
                .content(content)
                .build();
    }

}
