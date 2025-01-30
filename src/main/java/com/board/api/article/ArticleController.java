package com.board.api.article;

import com.board.api.ApiResponse;
import com.board.api.article.request.ArticleRequest;
import com.board.dto.page.PageRequest;
import com.board.dto.security.SecurityUser;
import com.board.service.article.ArticleService;
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
    public ApiResponse getArticle(@PathVariable Long id) {
        return ApiResponse.ok(articleService.getArticle(id));
    }

    @PutMapping("/{id}")
    public ApiResponse updateArticle(@PathVariable Long id, @RequestBody @Validated ArticleRequest request) {
        return ApiResponse.ok(articleService.updateArticle(request.toServiceRequest(id)));
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return ApiResponse.ok();
    }

    @GetMapping
    public ApiResponse getArticleList(PageRequest request) {
        return ApiResponse.ok(articleService.getArticleList(request.toServiceRequest()));
    }

}
