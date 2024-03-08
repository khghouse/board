package com.board.service.article;

import com.board.domain.article.Article;
import com.board.domain.article.ArticleRepository;
import com.board.exceptions.BusinessException;
import com.board.service.PageResponse;
import com.board.service.PageServiceRequest;
import com.board.service.article.request.ArticleCreateServiceRequest;
import com.board.service.article.request.ArticleUpdateServiceRequest;
import com.board.service.article.response.ArticleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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
        Article article = findById(id);
        return ArticleResponse.of(article);
    }

    /**
     * 게시글을 수정한다.
     */
    @Transactional
    public ArticleResponse putArticle(ArticleUpdateServiceRequest request) {
        Article article = findById(request.getId());
        article.update(request.getTitle(), request.getContent());
        return ArticleResponse.of(article);
    }

    /**
     * 게시글을 삭제한다.
     */
    public void deleteArticle(Long id) {
        Article article = findById(id);
        article.delete();
    }

    /**
     * 게시글 리스트를 조회한다.
     */
    public PageResponse getArticleList(PageServiceRequest request) {
        Page<Article> pageArticles;
        try {
            pageArticles = articleRepository.findAllByDeletedFalse(request.toPageable());
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }

        return PageResponse.of(
                pageArticles,
                pageArticles.getContent()
                        .stream()
                        .map(ArticleResponse::of)
                        .collect(Collectors.toList())
        );
    }

    private Article findById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("게시글 정보가 존재하지 않습니다."));
    }

}
