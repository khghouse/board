package com.board.domain.article;

import com.board.domain.member.Member;
import com.board.domain.member.MemberRepository;
import com.board.support.RepositoryTestSupport;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleLikeQueryRepositoryTest extends RepositoryTestSupport {

    private ArticleLikeQueryRepository articleLikeQueryRepository;

    @Autowired
    private ArticleLikeRepository articleLikeRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Article article;
    private Member member;

    @BeforeEach
    void setUp() {
        articleLikeQueryRepository = new ArticleLikeQueryRepository(queryFactory);

        member = Member.builder()
                .email("khghouse@daum.net")
                .password("Password12#$")
                .deleted(false)
                .build();
        memberRepository.save(member);

        article = Article.builder()
                .member(member)
                .title("제목입니다.")
                .content("내용입니다.")
                .deleted(false)
                .build();
        articleRepository.save(article);
    }

    @Test
    @DisplayName("특정 게시글에 좋아요를 누른 회원 목록을 조회하고 검증한다.")
    void findLikedMembers() {
        // given
        // 좋아요 + 1
        ArticleLike articleLike = ArticleLike.builder()
                .article(article)
                .member(member)
                .build();
        articleLikeRepository.save(articleLike);

        // 좋아요 + 1
        Member member2 = Member.builder()
                .email("khghouse@naver.com")
                .password("Password12#$")
                .deleted(false)
                .build();
        memberRepository.save(member2);

        ArticleLike articleLike2 = ArticleLike.builder()
                .article(article)
                .member(member2)
                .build();
        articleLikeRepository.save(articleLike2);

        // 다른 게시글에 좋아요 + 1
        Article anotherArticle = Article.builder()
                .member(member)
                .title("제목입니다.")
                .content("내용입니다.")
                .deleted(false)
                .build();
        articleRepository.save(anotherArticle);

        ArticleLike anotherArticleLike = ArticleLike.builder()
                .article(anotherArticle)
                .member(member)
                .build();
        articleLikeRepository.save(anotherArticleLike);

        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "id");

        // when
        Page<ArticleLike> result = articleLikeQueryRepository.findLikedMembers(article.getId(), pageable);

        // then
        assertThat(result.getContent()).hasSize(2)
                .extracting("article.id", "member.email")
                .containsExactly(
                        Tuple.tuple(article.getId(), "khghouse@naver.com"),
                        Tuple.tuple(article.getId(), "khghouse@daum.net")
                );
    }

    @Test
    @DisplayName("특정 게시글에 좋아요를 누른 회원 목록을 조회하지만 결과 값이 없다.")
    void findLikedMembersEmptyList() {
        // given
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "id");

        // when
        Page<ArticleLike> result = articleLikeQueryRepository.findLikedMembers(article.getId(), pageable);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("페이징 기능을 사용하지 않고 특정 게시글에 좋아요를 누른 회원 목록을 조회한다.")
    void findLikedMembersNotUsedPaging() {
        // given
        List<Member> members = IntStream.range(1, 21)
                .mapToObj(i -> toEntity("khghouse" + i + "@daum.net"))
                .collect(Collectors.toList());
        memberRepository.saveAll(members);

        List<ArticleLike> articleLikes = IntStream.range(1, 21)
                .mapToObj(i -> toEntity(members.get(i - 1)))
                .collect(Collectors.toList());
        articleLikeRepository.saveAll(articleLikes);

        // when
        Page<ArticleLike> result = articleLikeQueryRepository.findLikedMembers(article.getId(), Pageable.unpaged());

        // then
        assertThat(result.getContent()).hasSize(20);
    }

    private Member toEntity(String email) {
        return Member.builder()
                .email(email)
                .password("Password12#$")
                .deleted(false)
                .build();
    }

    private ArticleLike toEntity(Member member) {
        return ArticleLike.builder()
                .article(article)
                .member(member)
                .build();
    }

}