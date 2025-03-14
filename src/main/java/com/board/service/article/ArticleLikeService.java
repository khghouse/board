package com.board.service.article;

import com.board.domain.article.*;
import com.board.domain.member.Member;
import com.board.domain.member.MemberRepository;
import com.board.dto.page.PageResponseWithExtraData;
import com.board.dto.page.PageServiceRequest;
import com.board.exception.BusinessException;
import com.board.service.article.request.ArticleLikeServiceRequest;
import com.board.service.article.response.ArticleIdResponse;
import com.board.service.article.response.ArticleResponse;
import com.board.service.member.response.MemberIdResponse;
import com.board.service.member.response.MemberResponse;
import com.board.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.board.enumeration.ErrorCode.ARTICLE_NOT_FOUND;
import static com.board.enumeration.ErrorCode.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleLikeService {

    private final ArticleLikeRepository articleLikeRepository;
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    private final ArticleLikeQueryRepository articleLikeQueryRepository;

    @Transactional
    public void like(ArticleLikeServiceRequest request) {
        ArticleAndMember articleAndMember = getArticleAndMember(request.getArticleId(), request.getMemberId());

        if (!articleLikeRepository.existsByArticleAndMember(articleAndMember.article(), articleAndMember.member())) {
            articleLikeRepository.save(request.toEntity(articleAndMember.article(), articleAndMember.member()));
        }
    }

    @Transactional
    public void unlike(ArticleLikeServiceRequest request) {
        ArticleAndMember articleAndMember = getArticleAndMember(request.getArticleId(), request.getMemberId());
        articleLikeRepository.deleteByArticleAndMember(articleAndMember.article(), articleAndMember.member());
    }

    public PageResponseWithExtraData<ArticleIdResponse> getLikedMembers(Long articleId, PageServiceRequest request) {
        Page<ArticleLike> pageArticleLikes;
        try {
            pageArticleLikes = articleLikeQueryRepository.findLikedMembers(articleId, request.toPageable());
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }

        List<MemberResponse> members = CommonUtil.mapperToList(pageArticleLikes.getContent(), ArticleLike::getMember, MemberResponse::of);

        return PageResponseWithExtraData.of(pageArticleLikes, new ArticleIdResponse(articleId), members);
    }

    public PageResponseWithExtraData<MemberIdResponse> getLikedArticles(Long memberId, PageServiceRequest request) {
        Page<ArticleLike> pageArticleLikes;
        try {
            pageArticleLikes = articleLikeQueryRepository.findLikedArticles(memberId, request.toPageable());
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }

        List<ArticleResponse> articles = CommonUtil.mapperToList(pageArticleLikes.getContent(), ArticleLike::getArticle, ArticleResponse::of);

        return PageResponseWithExtraData.of(pageArticleLikes, new MemberIdResponse(memberId), articles);
    }

    private ArticleAndMember getArticleAndMember(Long articleId, Long memberId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BusinessException(ARTICLE_NOT_FOUND));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));

        return new ArticleAndMember(article, member);
    }

    private record ArticleAndMember(Article article, Member member) {
    }

}
