package com.board.api.article.request;

import com.board.service.article.request.ArticleServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleRequest {

    private Long id;

    @NotBlank(message = "게시글 제목을 입력해 주세요.")
    private String title;

    @NotBlank(message = "게시글 내용을 입력해 주세요.")
    private String content;

    public ArticleServiceRequest toServiceRequest() {
        return ArticleServiceRequest.of(id, title, content);
    }

    public void setId(Long id) {
        this.id = id;
    }

}
