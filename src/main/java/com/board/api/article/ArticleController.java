package com.board.api.article;

import com.board.api.ApiResponse;
import com.board.api.article.request.ArticleCreateRequest;
import com.board.service.article.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/articles")
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    public ApiResponse postArticle(@RequestBody ArticleCreateRequest request) {
        return ApiResponse.ok(articleService.postArticle(request.toServiceRequest()));
    }

}
