package com.board.api.article;

import com.board.api.ApiResponse;
import com.board.dto.page.PageRequest;
import com.board.api.article.request.ArticleRequest;
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
    public ApiResponse postArticle(@Validated @RequestBody ArticleRequest request) {
        return ApiResponse.ok(articleService.postArticle(request.toServiceRequest()));
    }

    @GetMapping("/{id}")
    public ApiResponse getArticle(@PathVariable Long id) {
        return ApiResponse.ok(articleService.getArticle(id));
    }

    @PutMapping("/{id}")
    public ApiResponse putArticle(@PathVariable Long id, @RequestBody @Validated ArticleRequest request) {
        request.setId(id);
        return ApiResponse.ok(articleService.putArticle(request.toServiceRequest()));
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
