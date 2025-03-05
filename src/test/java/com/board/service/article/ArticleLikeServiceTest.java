package com.board.service.article;

import com.board.domain.article.Article;
import com.board.domain.article.ArticleLike;
import com.board.domain.article.ArticleLikeRepository;
import com.board.domain.article.ArticleRepository;
import com.board.domain.member.Member;
import com.board.domain.member.MemberRepository;
import com.board.exception.BusinessException;
import com.board.service.article.request.ArticleLikeServiceRequest;
import com.board.support.IntegrationTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static com.board.enumeration.ErrorCode.ARTICLE_NOT_FOUND;
import static com.board.enumeration.ErrorCode.MEMBER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class ArticleLikeServiceTest extends IntegrationTestSupport {

    @Autowired
    private ArticleLikeService articleLikeService;

    @Autowired
    private ArticleLikeRepository articleLikeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ArticleRepository articleRepository;

    private Member member;
    private Article article;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .email("khghouse@daum.net")
                .password("Password12#$")
                .build();

        memberRepository.save(member);

        article = Article.builder()
                .title("안녕하세요.")
                .content("반갑습니다.")
                .deleted(false)
                .member(member)
                .build();

        articleRepository.save(article);
    }

    @Test
    @DisplayName("게시글 좋아요를 등록하고 검증한다.")
    void like() {
        // given
        ArticleLikeServiceRequest request = ArticleLikeServiceRequest.of(article.getId(), member.getId());

        // when
        articleLikeService.like(request);

        // then
        boolean result = articleLikeRepository.existsByArticleAndMember(article, member);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("이미 좋아요를 등록한 케이스를 검증한다.")
    void alreadyLiked() {
        // given
        ArticleLike articleLike = ArticleLike.builder()
                .article(article)
                .member(member)
                .build();
        articleLikeRepository.save(articleLike);

        long beforeCount = articleLikeRepository.count();

        ArticleLikeServiceRequest request = ArticleLikeServiceRequest.of(article.getId(), member.getId());

        // when
        articleLikeService.like(request);

        // then
        long afterCount = articleLikeRepository.count();
        assertThat(afterCount).isEqualTo(beforeCount);
    }

    @Test
    @DisplayName("게시글 좋아요를 등록할 때 게시글이 존재하지 않는다면 에러를 응답한다.")
    void likeNotFoundArticle() {
        // given
        ArticleLikeServiceRequest request = ArticleLikeServiceRequest.of(-1L, member.getId());

        // when, then
        assertThatThrownBy(() -> articleLikeService.like(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ARTICLE_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("게시글 좋아요를 등록할 때 회원 정보가 존재하지 않는다면 에러를 응답한다.")
    void likeNotFoundMember() {
        // given
        ArticleLikeServiceRequest request = ArticleLikeServiceRequest.of(article.getId(), -1L);

        // when, then
        assertThatThrownBy(() -> articleLikeService.like(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage(MEMBER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("게시글 좋아요를 취소하고 검증한다.")
    void unlike() {
        // given
        ArticleLike articleLike = ArticleLike.builder()
                .article(article)
                .member(member)
                .build();
        articleLikeRepository.save(articleLike);

        ArticleLikeServiceRequest request = ArticleLikeServiceRequest.of(article.getId(), member.getId());

        // when
        articleLikeService.unlike(request);

        // then
        boolean result = articleLikeRepository.existsByArticleAndMember(article, member);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("이미 좋아요를 취소한 케이스를 검증한다.")
    void alreadyUnLiked() {
        // given
        ArticleLikeServiceRequest request = ArticleLikeServiceRequest.of(article.getId(), member.getId());

        // when
        articleLikeService.unlike(request);

        // then
        boolean result = articleLikeRepository.existsByArticleAndMember(article, member);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("게시글 좋아요를 취소할 때 게시글이 존재하지 않는다면 에러를 응답한다.")
    void unlikeNotFoundArticle() {
        // given
        ArticleLikeServiceRequest request = ArticleLikeServiceRequest.of(-1L, member.getId());

        // when, then
        assertThatThrownBy(() -> articleLikeService.unlike(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ARTICLE_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("게시글 좋아요를 취소할 때 회원 정보가 존재하지 않는다면 에러를 응답한다.")
    void unlikeNotFoundMember() {
        // given
        ArticleLikeServiceRequest request = ArticleLikeServiceRequest.of(article.getId(), -1L);

        // when, then
        assertThatThrownBy(() -> articleLikeService.unlike(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage(MEMBER_NOT_FOUND.getMessage());
    }

}