package com.board.api.article.request;

import com.board.service.article.request.ArticleUpdateServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ArticleUpdateRequest {

    @Positive(message = "게시글 ID는 양수여야 합니다.")
    private Long id;

    @NotBlank(message = "게시글 제목을 입력해 주세요.")
    private String title;

    @NotBlank(message = "게시글 내용을 입력해 주세요.")
    private String content;

    @Builder
    private ArticleUpdateRequest(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public ArticleUpdateServiceRequest toServiceRequest() {
        return ArticleUpdateServiceRequest.builder()
                .id(id)
                .title(title)
                .content(content)
                .build();
    }

    public void setId(Long id) {
        this.id = id;
    }
    
}
