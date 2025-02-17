package com.board.service.article;

import com.board.domain.article.Article;
import com.board.domain.article.ArticleQueryRepository;
import com.board.domain.article.ArticleRepository;
import com.board.domain.member.Member;
import com.board.domain.member.MemberRepository;
import com.board.dto.page.PageResponse;
import com.board.dto.page.PageServiceRequest;
import com.board.exception.BusinessException;
import com.board.service.article.request.ArticleServiceRequest;
import com.board.service.article.response.ArticleDetailResponse;
import com.board.service.article.response.ArticleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

import static com.board.enumeration.ErrorCode.ARTICLE_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    private final ArticleQueryRepository articleQueryRepository;

    /**
     * 게시글을 등록한다.
     */
    @Transactional
    public ArticleResponse createArticle(ArticleServiceRequest request, Long memberId) {
        Member memberProxy = memberRepository.getReferenceById(memberId);
        Article article = articleRepository.save(request.toEntity(memberProxy));
        return ArticleResponse.of(article);
    }

    /**
     * 게시글 1건을 조회한다.
     */
    public ArticleDetailResponse getArticle(Long id) {
        Article article = findValidArticle(id);
        return ArticleDetailResponse.of(article);
    }

    /**
     * 게시글을 수정한다.
     */
    @Transactional
    public ArticleResponse updateArticle(ArticleServiceRequest request, Long memberId) {
        Article article = findValidArticle(request.getId());
        article.validateWriter(memberId);
        article.update(request.getTitle(), request.getContent());
        return ArticleResponse.of(article);
    }

    /**
     * 게시글을 삭제한다.
     */
    @Transactional
    public void deleteArticle(Long id, Long memberId) {
        Article article = findArticle(id);
        article.validateWriter(memberId);
        article.delete();
    }

    /**
     * 게시글 리스트를 조회한다.
     */
    public PageResponse getArticleList(PageServiceRequest request) {
        Page<Article> pageArticles;
        try {
            pageArticles = articleQueryRepository.findActiveArticles(request.toPageable());
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }

        return PageResponse.of(
                pageArticles,
                pageArticles.getContent()
                        .stream()
                        .map(ArticleDetailResponse::of)
                        .collect(Collectors.toList())
        );
    }

    private Article findValidArticle(Long id) {
        return articleRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new BusinessException(ARTICLE_NOT_FOUND));
    }

    private Article findArticle(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ARTICLE_NOT_FOUND));
    }

}
