package com.board.api;

import com.board.api.request.ArticleCreateRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/articles")
public class ArticleController {

    @PostMapping
    public String postArticle(@RequestBody ArticleCreateRequest request) {
        return "hello";
    }

}
