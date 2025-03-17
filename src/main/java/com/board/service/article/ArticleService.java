package com.board.service.article;

import com.board.component.Redis;
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
import com.board.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

import static com.board.enumeration.ErrorCode.ARTICLE_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {

    public static final String PREFIX_REDIS_KEY_ARTICLE_VIEW = "articleView:";
    public static final long ARTICLE_VIEW_INCREMENT_INTERVAL_IN_MILLIS = 60 * 60 * 1000;

    private final Redis redis;

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
    public ArticleDetailResponse getArticle(Long id, String clientIp) {
        Article article = findValidArticle(id);
        if (isIncrementViewCount(id, clientIp)) {
            article.incrementViewCount();
        }
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

        return PageResponse.of(pageArticles, CommonUtil.mapperToList(pageArticles.getContent(), ArticleDetailResponse::of));
    }

    private Article findValidArticle(Long id) {
        return articleRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new BusinessException(ARTICLE_NOT_FOUND));
    }

    private Article findArticle(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ARTICLE_NOT_FOUND));
    }

    private boolean isIncrementViewCount(Long id, String clientIp) {
        String key = PREFIX_REDIS_KEY_ARTICLE_VIEW + id + ":" + clientIp;
        long currentTime = System.currentTimeMillis();

        String lastViewTimeStr = redis.get(key);
        long lastViewTime = lastViewTimeStr != null ? Long.parseLong(lastViewTimeStr) : 0;

        if (lastViewTime == 0 || (currentTime - lastViewTime) >= ARTICLE_VIEW_INCREMENT_INTERVAL_IN_MILLIS) {
            redis.set(key, String.valueOf(currentTime), ARTICLE_VIEW_INCREMENT_INTERVAL_IN_MILLIS, TimeUnit.MILLISECONDS);
            return true;
        }
        return false;
    }

}
