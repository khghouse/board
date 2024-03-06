package com.board.api.article;

import com.board.api.ApiResponse;
import com.board.api.article.request.ArticleCreateRequest;
import com.board.api.article.request.ArticleRequest;
import com.board.api.article.request.ArticleUpdateRequest;
import com.board.service.article.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/articles")
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    public ApiResponse postArticle(@Validated @RequestBody ArticleCreateRequest request) {
        return ApiResponse.ok(articleService.postArticle(request.toServiceRequest()));
    }

    @GetMapping("/{id}")
    public ApiResponse getArticle(@Validated ArticleRequest request) {
        return ApiResponse.ok(articleService.getArticle(request.getId()));
    }

    @PutMapping("/{id}")
    public ApiResponse putArticle(@Validated @RequestBody ArticleUpdateRequest request) {
        return ApiResponse.ok(articleService.putArticle(request.toServiceRequest()));
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteArticle(@Validated ArticleRequest request) {
        articleService.deleteArticle(request.getId());
        return ApiResponse.ok();
    }

    @GetMapping
    public ApiResponse getArticleList() {
        return ApiResponse.ok(articleService.getArticleList());
    }

}
