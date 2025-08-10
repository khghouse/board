package com.board.domain.article.controller;

import com.board.global.common.dto.ApiResponse;
import com.board.domain.article.dto.request.ArticleRequest;
import com.board.global.common.dto.page.PageRequest;
import com.board.global.security.SecurityUser;
import com.board.domain.article.service.ArticleService;
import com.board.global.common.util.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/articles")
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    public ApiResponse createArticle(@Validated @RequestBody ArticleRequest request, @AuthenticationPrincipal SecurityUser securityUser) {
        return ApiResponse.ok(articleService.createArticle(request.toServiceRequest(), securityUser.getMemberId()));
    }

    @GetMapping("/{id}")
    public ApiResponse getArticle(@PathVariable Long id, HttpServletRequest request) {
        return ApiResponse.ok(articleService.getArticle(id, CommonUtil.getClientIp(request)));
    }

    @PutMapping("/{id}")
    public ApiResponse updateArticle(@PathVariable Long id, @RequestBody @Validated ArticleRequest request, @AuthenticationPrincipal SecurityUser securityUser) {
        return ApiResponse.ok(articleService.updateArticle(request.toServiceRequest(id), securityUser.getMemberId()));
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteArticle(@PathVariable Long id, @AuthenticationPrincipal SecurityUser securityUser) {
        articleService.deleteArticle(id, securityUser.getMemberId());
        return ApiResponse.ok();
    }

    @GetMapping
    public ApiResponse getArticleList(PageRequest request) {
        return ApiResponse.ok(articleService.getArticleList(request.toServiceRequest()));
    }

}
