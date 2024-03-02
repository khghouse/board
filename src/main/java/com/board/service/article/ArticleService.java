package com.board.service.article;

import com.board.domain.article.Article;
import com.board.domain.article.ArticleRepository;
import com.board.service.article.request.ArticleCreateServiceRequest;
import com.board.service.article.response.ArticleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    /**
     * 게시글을 등록한다.
     */
    public ArticleResponse postArticle(ArticleCreateServiceRequest request) {
        Article article = articleRepository.save(request.toEntity());
        return ArticleResponse.of(article);
    }

    /**
     * 게시글 1건을 조회한다.s
     */
    public ArticleResponse getArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("게시글 정보가 존재하지 않습니다."));

        return ArticleResponse.of(article);
    }

}
