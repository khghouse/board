package com.board.api.article.request;

import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ArticleRequest {

    @Positive(message = "게시글 ID는 양수여야 합니다.")
    private Long id;

    @Builder
    private ArticleRequest(Long id) {
        this.id = id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
