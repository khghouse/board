package com.board.domain.article.controller;

import com.board.domain.article.dto.request.ArticleLikeServiceRequest;
import com.board.domain.article.dto.response.ArticleIdResponse;
import com.board.domain.article.service.ArticleLikeService;
import com.board.domain.member.dto.response.MemberIdResponse;
import com.board.global.common.dto.ApiResponse;
import com.board.global.common.dto.page.PageResponseWithExtraData;
import com.board.global.common.dto.page.PageServiceRequest;
import com.board.global.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ArticleLikeController {

    private final ArticleLikeService articleLikeService;

    @PostMapping("/articles/{id}/likes")
    public ApiResponse<Void> like(@PathVariable Long id, @AuthenticationPrincipal SecurityUser securityUser) {
        articleLikeService.like(ArticleLikeServiceRequest.of(id, securityUser.getMemberId()));
        return ApiResponse.ok();
    }

    @DeleteMapping("/articles/{id}/likes")
    public ApiResponse<Void> unlike(@PathVariable Long id, @AuthenticationPrincipal SecurityUser securityUser) {
        articleLikeService.unlike(ArticleLikeServiceRequest.of(id, securityUser.getMemberId()));
        return ApiResponse.ok();
    }

    @GetMapping("/articles/{id}/likes/members")
    public ApiResponse<PageResponseWithExtraData<ArticleIdResponse>> getLikedMembers(@PathVariable Long id) {
        return ApiResponse.ok(articleLikeService.getLikedMembers(id, PageServiceRequest.withDefault()));
    }

    @GetMapping("/members/{id}/likes/articles")
    public ApiResponse<PageResponseWithExtraData<MemberIdResponse>> getLikedArticles(@PathVariable Long id) {
        return ApiResponse.ok(articleLikeService.getLikedArticles(id, PageServiceRequest.withDefault()));
    }

}
