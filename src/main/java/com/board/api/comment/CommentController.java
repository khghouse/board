package com.board.api.comment;

import com.board.api.ApiResponse;
import com.board.api.comment.request.CommentRequest;
import com.board.dto.security.SecurityUser;
import com.board.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/articles/{articleId}/comments")
    public ApiResponse createComment(@PathVariable Long articleId, @Validated @RequestBody CommentRequest request, @AuthenticationPrincipal SecurityUser securityUser) {
        commentService.createComment(request.toServiceRequest(articleId), securityUser.getMemberId());
        return ApiResponse.ok();
    }

}
