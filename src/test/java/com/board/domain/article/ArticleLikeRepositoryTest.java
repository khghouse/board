package com.board.domain.article;

import com.board.domain.member.Member;
import com.board.domain.member.MemberRepository;
import com.board.support.RepositoryTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleLikeRepositoryTest extends RepositoryTestSupport {

    @Autowired
    private ArticleLikeRepository articleLikeRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Article article;

    @BeforeEach
    void setUp() {
        Member member = Member.builder()
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
    @DisplayName("게시글 좋아요를 등록, 조회하고 검증한다.")
    void saveAndFindById() {
        // given
        Member member = Member.builder()
                .email("khghouse@naver.com")
                .password("Password12#$")
                .build();

        memberRepository.save(member);

        ArticleLike articleLike = ArticleLike.builder()
                .article(article)
                .member(member)
                .build();

        // when
        ArticleLike result = articleLikeRepository.save(articleLike);

        // then
        assertThat(result).extracting("id", "article.id", "article.member.email", "member.email")
                .contains(articleLike.getId(), article.getId(), "khghouse@daum.net", "khghouse@naver.com");
    }

    @Test
    @DisplayName("(물리 삭제) 게시글 좋아요를 삭제하고 검증한다.")
    void delete() {
        // given
        Member member = Member.builder()
                .email("khghouse@naver.com")
                .password("Password12#$")
                .build();
        memberRepository.save(member);

        ArticleLike articleLike = ArticleLike.builder()
                .article(article)
                .member(member)
                .build();
        articleLikeRepository.save(articleLike);

        // when
        articleLikeRepository.delete(articleLike);

        // then
        assertThat(articleLikeRepository.existsById(articleLike.getId())).isFalse();
    }

}