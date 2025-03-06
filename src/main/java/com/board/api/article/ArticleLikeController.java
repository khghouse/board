package com.board.api.article;

import com.board.api.ApiResponse;
import com.board.dto.security.SecurityUser;
import com.board.service.article.ArticleLikeService;
import com.board.service.article.request.ArticleLikeServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/articles/{id}/likes")
public class ArticleLikeController {

    private final ArticleLikeService articleLikeService;

    @PostMapping
    public ApiResponse like(@PathVariable Long id, @AuthenticationPrincipal SecurityUser securityUser) {
        articleLikeService.like(ArticleLikeServiceRequest.of(id, securityUser.getMemberId()));
        return ApiResponse.ok();
    }

    @DeleteMapping
    public ApiResponse unlike(@PathVariable Long id, @AuthenticationPrincipal SecurityUser securityUser) {
        articleLikeService.unlike(ArticleLikeServiceRequest.of(id, securityUser.getMemberId()));
        return ApiResponse.ok();
    }

}
