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

class ArticleQueryRepositoryTest extends RepositoryTestSupport {

    private ArticleQueryRepository articleQueryRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        articleQueryRepository = new ArticleQueryRepository(queryFactory);
    }

    @Test
    @DisplayName("현재 유효한 게시글 리스트만 조회하고 검증한다.")
    void findActiveArticles() {
        // given
        List<Member> members = IntStream.range(1, 5)
                .mapToObj(i -> toEntity("khghouse" + i + "@daum.net"))
                .collect(Collectors.toList());
        memberRepository.saveAll(members);

        Article article1 = toEntity("게시글 제목 1", "게시글 내용 1", false, members.get(0));
        Article article2 = toEntity("게시글 제목 2", "게시글 내용 2", true, members.get(1));
        Article article3 = toEntity("게시글 제목 3", "게시글 내용 3", false, members.get(2));
        Article article4 = toEntity("게시글 제목 4", "게시글 내용 4", false, members.get(3));
        articleRepository.saveAll(List.of(article1, article2, article3, article4));

        testEntityManager.flush();
        testEntityManager.clear();
        System.out.println("[N+1] 이슈 체크");

        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "id");

        // when
        Page<Article> result = articleQueryRepository.findActiveArticles(pageable);

        // then
        assertThat(result.getContent()).hasSize(3)
                .extracting("title", "content", "member.email")
                .containsExactly(
                        Tuple.tuple("게시글 제목 4", "게시글 내용 4", "khghouse4@daum.net"),
                        Tuple.tuple("게시글 제목 3", "게시글 내용 3", "khghouse3@daum.net"),
                        Tuple.tuple("게시글 제목 1", "게시글 내용 1", "khghouse1@daum.net")
                );
    }

    @Test
    @DisplayName("현재 유효한 게시글 리스트만 조회하고 검증한다.")
    void findActiveArticlesWithPaging() {
        // given
        List<Member> members = IntStream.range(1, 21)
                .mapToObj(i -> toEntity("khghouse" + i + "@daum.net"))
                .collect(Collectors.toList());
        memberRepository.saveAll(members);

        List<Article> articles = IntStream.range(1, 21)
                .mapToObj(i -> toEntity("게시글 제목 " + i, "게시글 내용 " + i, false, members.get(i - 1)))
                .collect(Collectors.toList());
        articleRepository.saveAll(articles);

        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "id");

        // when
        Page<Article> result = articleQueryRepository.findActiveArticles(pageable);

        // then
        assertThat(result.getTotalPages()).isEqualTo(4);
        assertThat(result.getTotalElements()).isEqualTo(20);
        assertThat(result.getContent()).hasSize(5)
                .extracting("title", "content", "member.email")
                .containsExactly(
                        Tuple.tuple("게시글 제목 20", "게시글 내용 20", "khghouse20@daum.net"),
                        Tuple.tuple("게시글 제목 19", "게시글 내용 19", "khghouse19@daum.net"),
                        Tuple.tuple("게시글 제목 18", "게시글 내용 18", "khghouse18@daum.net"),
                        Tuple.tuple("게시글 제목 17", "게시글 내용 17", "khghouse17@daum.net"),
                        Tuple.tuple("게시글 제목 16", "게시글 내용 16", "khghouse16@daum.net")
                );
    }

    @Test
    @DisplayName("페이징 처리된 게시글 리스트를 조회하지만 결과 값이 없다.")
    void findActiveArticlesEmptyList() {
        // given
        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "id");

        // when
        Page<Article> result = articleQueryRepository.findActiveArticles(pageable);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("페이징 기능을 사용하지 않고 현재 유효한 게시글 리스트를 조회한다.")
    void findActiveArticlesNotUsedPaging() {
        // given
        List<Member> members = IntStream.range(1, 21)
                .mapToObj(i -> toEntity("khghouse" + i + "@daum.net"))
                .collect(Collectors.toList());
        memberRepository.saveAll(members);

        List<Article> articles = IntStream.range(1, 21)
                .mapToObj(i -> toEntity("게시글 제목 " + i, "게시글 내용 " + i, false, members.get(i - 1)))
                .collect(Collectors.toList());
        articleRepository.saveAll(articles);

        // when
        Page<Article> result = articleQueryRepository.findActiveArticles(Pageable.unpaged());

        // then
        assertThat(result.getContent()).hasSize(20);
    }

    private Article toEntity(String title, String content, boolean deleted, Member member) {
        return Article.builder()
                .title(title)
                .content(content)
                .deleted(deleted)
                .member(member)
                .build();
    }

    private Member toEntity(String email) {
        return Member.builder()
                .email(email)
                .password("Password12#$")
                .build();
    }

}