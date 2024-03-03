package com.board.service.article;

import com.board.domain.article.Article;
import com.board.domain.article.ArticleRepository;
import com.board.service.article.request.ArticleCreateServiceRequest;
import com.board.service.article.request.ArticleUpdateServiceRequest;
import com.board.service.article.response.ArticleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {

    private final ArticleRepository articleRepository;

    /**
     * 게시글을 등록한다.
     */
    @Transactional
    public ArticleResponse postArticle(ArticleCreateServiceRequest request) {
        Article article = articleRepository.save(request.toEntity());
        return ArticleResponse.of(article);
    }

    /**
     * 게시글 1건을 조회한다.
     */
    public ArticleResponse getArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("게시글 정보가 존재하지 않습니다."));

        return ArticleResponse.of(article);
    }

    /**
     * 게시글을 수정한다.
     */
    @Transactional
    public ArticleResponse putArticle(ArticleUpdateServiceRequest request) {
        Article article = articleRepository.findById(request.getId())
                .orElseThrow(() -> new NoSuchElementException("게시글 정보가 존재하지 않습니다."));

        article.update(request.getTitle(), request.getContent());
        return ArticleResponse.of(article);
    }

}
