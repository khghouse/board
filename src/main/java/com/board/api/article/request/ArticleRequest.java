package com.board.api.article.request;

import com.board.service.article.request.ArticleServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleRequest {

    private Long id;

    @NotBlank(message = "게시글 제목을 입력해 주세요.")
    private String title;

    @NotBlank(message = "게시글 내용을 입력해 주세요.")
    private String content;

    public ArticleServiceRequest toServiceRequest() {
        return ArticleServiceRequest.builder()
                .id(id)
                .title(title)
                .content(content)
                .build();
    }

    public void setId(Long id) {
        this.id = id;
    }

}
