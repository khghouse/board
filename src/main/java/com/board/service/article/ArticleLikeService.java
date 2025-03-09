package com.board.service.article;

import com.board.domain.article.*;
import com.board.domain.member.Member;
import com.board.domain.member.MemberRepository;
import com.board.dto.page.PageResponseWithExtraData;
import com.board.dto.page.PageServiceRequest;
import com.board.exception.BusinessException;
import com.board.service.article.request.ArticleLikeServiceRequest;
import com.board.service.member.response.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    public PageResponseWithExtraData<Long> getLikedMembers(Long articleId, PageServiceRequest request) {
        Page<ArticleLike> pageArticleLikes;
        try {
            pageArticleLikes = articleLikeQueryRepository.findLikedMembers(articleId, request.toPageable());
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }

        List<MemberResponse> members = pageArticleLikes.getContent()
                .stream()
                .map(ArticleLike::getMember)
                .map(MemberResponse::of)
                .collect(Collectors.toList());

        return PageResponseWithExtraData.of(pageArticleLikes, articleId, members);
    }

    private record ArticleAndMember(Article article, Member member) {
    }

    private ArticleAndMember getArticleAndMember(Long articleId, Long memberId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BusinessException(ARTICLE_NOT_FOUND));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));

        return new ArticleAndMember(article, member);
    }

}
