package com.board.api.article.request;

import com.board.service.article.request.ArticleCreateServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ArticleCreateRequest {

    @NotBlank(message = "게시글 제목을 입력해 주세요.")
    private String title;

    @NotBlank(message = "게시글 내용을 입력해 주세요.")
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
