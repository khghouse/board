package com.board.domain.article;

import com.board.domain.member.Member;
import com.board.domain.member.MemberRepository;
import com.board.dto.page.PageServiceRequest;
import com.board.support.RepositoryTestSupport;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
    @DisplayName("querydsl 샘플 테스트 코드")
    void querydslSampleTestCode() {
        // given
        List<Member> members = IntStream.range(1, 5)
                .mapToObj(i -> Member.builder().email("khghouse" + i + "@daum.net").password("Password12#$").build())
                .collect(Collectors.toList());
        memberRepository.saveAll(members);

        Article article1 = toEntity("게시글 제목 1", "게시글 내용 1", false, members.get(0));
        Article article2 = toEntity("게시글 제목 2", "게시글 내용 2", true, members.get(1));
        Article article3 = toEntity("게시글 제목 3", "게시글 내용 3", false, members.get(2));
        Article article4 = toEntity("게시글 제목 4", "게시글 내용 4", false, members.get(3));
        articleRepository.saveAll(List.of(article1, article2, article3, article4));

        testEntityManager.flush();
        testEntityManager.clear();
        System.out.println("---------------------------");

        // when
        List<Article> result = articleQueryRepository.findAllWithMember();

        // then
        assertThat(result).hasSize(3)
                .extracting("title", "content", "member.email")
                .containsExactly(
                        Tuple.tuple("게시글 제목 1", "게시글 내용 1", "khghouse1@daum.net"),
                        Tuple.tuple("게시글 제목 3", "게시글 내용 3", "khghouse3@daum.net"),
                        Tuple.tuple("게시글 제목 4", "게시글 내용 4", "khghouse4@daum.net")
                );
    }

    @Test
    @DisplayName("querydsl 페이징 처리 테스트")
    void findActiveArticles() {
        // given
        List<Member> members = IntStream.range(1, 5)
                .mapToObj(i -> Member.builder().email("khghouse" + i + "@daum.net").password("Password12#$").build())
                .collect(Collectors.toList());
        memberRepository.saveAll(members);

        Article article1 = toEntity("게시글 제목 1", "게시글 내용 1", false, members.get(0));
        Article article2 = toEntity("게시글 제목 2", "게시글 내용 2", true, members.get(1));
        Article article3 = toEntity("게시글 제목 3", "게시글 내용 3", false, members.get(2));
        Article article4 = toEntity("게시글 제목 4", "게시글 내용 4", false, members.get(3));
        articleRepository.saveAll(List.of(article1, article2, article3, article4));

        testEntityManager.flush();
        testEntityManager.clear();
        System.out.println("---------------------------");

        PageServiceRequest request = PageServiceRequest.of(1, 10, "desc", "id");

        // when
        List<Article> result = articleQueryRepository.findActiveArticles(request.toPageable());

        // then
        assertThat(result).hasSize(3)
                .extracting("title", "content", "member.email")
                .containsExactly(
                        Tuple.tuple("게시글 제목 4", "게시글 내용 4", "khghouse4@daum.net"),
                        Tuple.tuple("게시글 제목 3", "게시글 내용 3", "khghouse3@daum.net"),
                        Tuple.tuple("게시글 제목 1", "게시글 내용 1", "khghouse1@daum.net")
                );
    }

    private static Article toEntity(String title, String content, boolean deleted, Member member) {
        return Article.builder()
                .title(title)
                .content(content)
                .deleted(deleted)
                .member(member)
                .build();
    }

}