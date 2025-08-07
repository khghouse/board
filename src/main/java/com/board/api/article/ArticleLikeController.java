package com.board.api.article;

import com.board.dto.ApiResponse;
import com.board.dto.page.PageServiceRequest;
import com.board.dto.security.SecurityUser;
import com.board.service.article.ArticleLikeService;
import com.board.service.article.request.ArticleLikeServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ArticleLikeController {

    private final ArticleLikeService articleLikeService;

    @PostMapping("/articles/{id}/likes")
    public ApiResponse like(@PathVariable Long id, @AuthenticationPrincipal SecurityUser securityUser) {
        articleLikeService.like(ArticleLikeServiceRequest.of(id, securityUser.getMemberId()));
        return ApiResponse.ok();
    }

    @DeleteMapping("/articles/{id}/likes")
    public ApiResponse unlike(@PathVariable Long id, @AuthenticationPrincipal SecurityUser securityUser) {
        articleLikeService.unlike(ArticleLikeServiceRequest.of(id, securityUser.getMemberId()));
        return ApiResponse.ok();
    }

    @GetMapping("/articles/{id}/likes/members")
    public ApiResponse getLikedMembers(@PathVariable Long id) {
        return ApiResponse.ok(articleLikeService.getLikedMembers(id, PageServiceRequest.withDefault()));
    }

    @GetMapping("/members/{id}/likes/articles")
    public ApiResponse getLikedArticles(@PathVariable Long id) {
        return ApiResponse.ok(articleLikeService.getLikedArticles(id, PageServiceRequest.withDefault()));
    }

}
