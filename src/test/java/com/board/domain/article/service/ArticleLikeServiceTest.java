package com.board.domain.article.service;

import com.board.domain.article.dto.request.ArticleLikeServiceRequest;
import com.board.domain.article.dto.response.ArticleIdResponse;
import com.board.domain.article.entity.Article;
import com.board.domain.article.entity.ArticleLike;
import com.board.domain.article.repository.ArticleLikeRepository;
import com.board.domain.article.repository.ArticleRepository;
import com.board.domain.article.service.ArticleLikeService;
import com.board.domain.member.dto.response.MemberIdResponse;
import com.board.domain.member.entity.Member;
import com.board.domain.member.repository.MemberRepository;
import com.board.global.common.dto.page.PageResponseWithExtraData;
import com.board.global.common.dto.page.PageServiceRequest;
import com.board.global.common.exception.NotFoundException;
import com.board.support.IntegrationTestSupport;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.board.global.common.enumeration.ErrorCode.ARTICLE_NOT_FOUND;
import static com.board.global.common.enumeration.ErrorCode.MEMBER_NOT_FOUND;
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
    @DisplayName("게시글 좋아요를 추가하고 검증한다.")
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
    @DisplayName("이미 좋아요를 추가한 케이스를 검증한다.")
    void alreadyLiked() {
        // given
        ArticleLike articleLike = toEntity(member, article);
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
    @DisplayName("게시글 좋아요를 추가할 때 게시글이 존재하지 않는다면 에러를 응답한다.")
    void likeNotFoundArticle() {
        // given
        ArticleLikeServiceRequest request = ArticleLikeServiceRequest.of(-1L, member.getId());

        // when, then
        assertThatThrownBy(() -> articleLikeService.like(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ARTICLE_NOT_FOUND.getMessage()); // 게시글 정보가 존재하지 않습니다.
    }

    @Test
    @DisplayName("게시글 좋아요를 추가할 때 회원 정보가 존재하지 않는다면 에러를 응답한다.")
    void likeNotFoundMember() {
        // given
        ArticleLikeServiceRequest request = ArticleLikeServiceRequest.of(article.getId(), -1L);

        // when, then
        assertThatThrownBy(() -> articleLikeService.like(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(MEMBER_NOT_FOUND.getMessage()); // 존재하지 않는 계정입니다.
    }

    @Test
    @DisplayName("게시글 좋아요를 취소하고 검증한다.")
    void unlike() {
        // given
        ArticleLike articleLike = toEntity(member, article);
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
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ARTICLE_NOT_FOUND.getMessage()); // 게시글 정보가 존재하지 않습니다.
    }

    @Test
    @DisplayName("게시글 좋아요를 취소할 때 회원 정보가 존재하지 않는다면 에러를 응답한다.")
    void unlikeNotFoundMember() {
        // given
        ArticleLikeServiceRequest request = ArticleLikeServiceRequest.of(article.getId(), -1L);

        // when, then
        assertThatThrownBy(() -> articleLikeService.unlike(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(MEMBER_NOT_FOUND.getMessage()); // 존재하지 않는 계정입니다.
    }

    @Test
    @DisplayName("특정 게시글에 좋아요를 누른 회원 목록을 조회하고 검증한다.")
    void getLikedMembers() {
        // given
        List<Member> members = IntStream.range(1, 6)
                .mapToObj(i -> toEntity("khghouse" + i + "@daum.net"))
                .collect(Collectors.toList());
        memberRepository.saveAll(members);

        List<ArticleLike> articleLikes = IntStream.range(1, 6)
                .mapToObj(i -> toEntity(members.get(i - 1), article))
                .collect(Collectors.toList());
        articleLikeRepository.saveAll(articleLikes);

        // when
        PageResponseWithExtraData<ArticleIdResponse> result = articleLikeService.getLikedMembers(article.getId(), PageServiceRequest.withDefault());

        // then
        assertThat(result.getPageInformation().getPageNumber()).isEqualTo(1);
        assertThat(result.getPageInformation().getTotalPages()).isEqualTo(1);
        assertThat(result.getPageInformation().getTotalElements()).isEqualTo(5);
        assertThat(result.getPageInformation().getIsLast()).isTrue();
        assertThat(result.getExtraData()).isEqualTo(new ArticleIdResponse(article.getId()));
        assertThat(result.getContents()).hasSize(5)
                .extracting("email")
                .containsExactly(
                        "khghouse5@daum.net",
                        "khghouse4@daum.net",
                        "khghouse3@daum.net",
                        "khghouse2@daum.net",
                        "khghouse1@daum.net"
                );
    }

    @Test
    @DisplayName("리스트 사이즈가 0이면 빈 배열을 응답한다.")
    void getLikedMembersEmptyList() {
        // when
        PageResponseWithExtraData<ArticleIdResponse> result = articleLikeService.getLikedMembers(article.getId(), PageServiceRequest.withDefault());

        // then
        assertThat(result.getPageInformation().getPageNumber()).isEqualTo(1);
        assertThat(result.getPageInformation().getTotalPages()).isEqualTo(1);
        assertThat(result.getPageInformation().getTotalElements()).isEqualTo(0);
        assertThat(result.getPageInformation().getIsLast()).isTrue();
        assertThat(result.getExtraData()).isEqualTo(new ArticleIdResponse(article.getId()));
        assertThat(result.getContents()).hasSize(0);
    }

    @Test
    @DisplayName("특정 회원이 좋아요한 게시글 목록을 조회하고 검증한다.")
    void getLikedArticles() {
        // given
        List<Article> articles = IntStream.range(1, 6)
                .mapToObj(i -> toEntity("제목입니다" + i, "내용입니다" + i, member))
                .collect(Collectors.toList());
        articleRepository.saveAll(articles);

        List<ArticleLike> articleLikes = IntStream.range(1, 6)
                .mapToObj(i -> toEntity(member, articles.get(i - 1)))
                .collect(Collectors.toList());
        articleLikeRepository.saveAll(articleLikes);

        // when
        PageResponseWithExtraData<MemberIdResponse> result = articleLikeService.getLikedArticles(member.getId(), PageServiceRequest.withDefault());

        // then
        assertThat(result.getPageInformation().getPageNumber()).isEqualTo(1);
        assertThat(result.getPageInformation().getTotalPages()).isEqualTo(1);
        assertThat(result.getPageInformation().getTotalElements()).isEqualTo(5);
        assertThat(result.getPageInformation().getIsLast()).isTrue();
        assertThat(result.getExtraData()).isEqualTo(new MemberIdResponse(member.getId()));
        assertThat(result.getContents()).hasSize(5)
                .extracting("title", "content")
                .containsExactly(
                        Tuple.tuple("제목입니다5", "내용입니다5"),
                        Tuple.tuple("제목입니다4", "내용입니다4"),
                        Tuple.tuple("제목입니다3", "내용입니다3"),
                        Tuple.tuple("제목입니다2", "내용입니다2"),
                        Tuple.tuple("제목입니다1", "내용입니다1")
                );
    }

    @Test
    @DisplayName("리스트 사이즈가 0이면 빈 배열을 응답한다.")
    void getLikedArticlesEmptyList() {
        // when
        PageResponseWithExtraData<MemberIdResponse> result = articleLikeService.getLikedArticles(member.getId(), PageServiceRequest.withDefault());

        // then
        assertThat(result.getPageInformation().getPageNumber()).isEqualTo(1);
        assertThat(result.getPageInformation().getTotalPages()).isEqualTo(1);
        assertThat(result.getPageInformation().getTotalElements()).isEqualTo(0);
        assertThat(result.getPageInformation().getIsLast()).isTrue();
        assertThat(result.getExtraData()).isEqualTo(new MemberIdResponse(member.getId()));
        assertThat(result.getContents()).hasSize(0);
    }

    private Member toEntity(String email) {
        return Member.builder()
                .email(email)
                .password("Password12#$")
                .deleted(false)
                .build();
    }

    private ArticleLike toEntity(Member member, Article article) {
        return ArticleLike.builder()
                .article(article)
                .member(member)
                .build();
    }

    private Article toEntity(String title, String content, Member member) {
        return Article.builder()
                .title(title)
                .content(content)
                .deleted(false)
                .member(member)
                .build();
    }

}