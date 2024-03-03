package com.board.service.article.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ArticleUpdateServiceRequest {

    private Long id;
    private String title;
    private String content;

    @Builder
    private ArticleUpdateServiceRequest(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

}
