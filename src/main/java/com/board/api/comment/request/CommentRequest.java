package com.board.api.comment.request;

import com.board.service.comment.request.CommentServiceRequest;
import com.board.validation.OnCreate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentRequest {

    private Long id;

    @NotNull(message = "게시글 ID는 필수입니다.", groups = OnCreate.class)
    @Positive(message = "게시글 ID는 0보다 큰 숫자여야 합니다.", groups = OnCreate.class)
    private Long articleId;

    @NotBlank(message = "댓글 내용을 입력해 주세요.", groups = OnCreate.class)
    private String content;

    public CommentServiceRequest toServiceRequest() {
        return CommentServiceRequest.withContentAndArticle(content, articleId);
    }

}
