package com.board.service.article.request;

import com.board.domain.article.Article;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleServiceRequest {

    private Long id;
    private String title;
    private String content;

    public Article toEntity() {
        return Article.builder()
                .title(title)
                .content(content)
                .deleted(false)
                .build();
    }

}
