package com.board.domain.comment.controller;

import com.board.global.common.dto.ApiResponse;
import com.board.domain.comment.dto.request.CommentRequest;
import com.board.global.security.SecurityUser;
import com.board.domain.comment.service.CommentService;
import com.board.global.common.validation.OnCreate;
import com.board.global.common.validation.OnUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ApiResponse createComment(@Validated(OnCreate.class) @RequestBody CommentRequest request, @AuthenticationPrincipal SecurityUser securityUser) {
        commentService.createComment(request.toServiceRequest(), securityUser.getMemberId());
        return ApiResponse.ok();
    }

    @GetMapping("/{id}")
    public ApiResponse getComment(@PathVariable Long id) {
        return ApiResponse.ok(commentService.getComment(id));
    }

    @PutMapping("/{id}")
    public ApiResponse updateComment(@PathVariable Long id, @Validated(OnUpdate.class) @RequestBody CommentRequest request, @AuthenticationPrincipal SecurityUser securityUser) {
        commentService.updateComment(request.toServiceRequest(id), securityUser.getMemberId());
        return ApiResponse.ok();
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteComment(@PathVariable Long id, @AuthenticationPrincipal SecurityUser securityUser) {
        commentService.deleteComment(id, securityUser.getMemberId());
        return ApiResponse.ok();
    }

}
