package com.board.api.article.request;

import com.board.service.article.request.ArticleCreateServiceRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ArticleCreateRequest {

    private String title;
    private String content;

    @Builder
    private ArticleCreateRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public ArticleCreateServiceRequest toServiceRequest() {
        return ArticleCreateServiceRequest.builder()
                .title(title)
                .content(content)
                .build();
    }

}
