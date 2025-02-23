package com.board.api.comment.request;

import com.board.service.comment.request.CommentServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentRequest {

    private Long id;

    @NotBlank(message = "댓글 내용을 입력해 주세요.")
    private String content;

    public CommentServiceRequest toServiceRequest(Long articleId) {
        return CommentServiceRequest.withContentAndArticle(content, articleId);
    }

}
