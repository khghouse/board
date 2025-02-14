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
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ArticleRepositoryTest extends RepositoryTestSupport {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .email("khghouse@daum.net")
                .password("Password12#$")
                .build();

        memberRepository.save(member);
    }

    @Test
    @DisplayName("게시글을 등록, 조회하고 검증한다.")
    void saveAndfindById() {
        // given
        Article article = toEntity("게시글 제목", "게시글 내용");

        // when
        Article result = articleRepository.save(article);

        // then
        assertThat(result).extracting("id", "title", "content", "member.email")
                .contains(article.getId(), "게시글 제목", "게시글 내용", "khghouse@daum.net");
    }

    @Test
    @DisplayName("게시글 1건의 조회 결과가 없어서 에러가 발생한다.")
    void notFindById() {
        // when, then
        assertThatThrownBy(() -> articleRepository.findById(1L).orElseThrow())
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("게시글을 수정하고 검증한다.")
    void update() {
        // given
        Article article = toEntity("게시글 제목", "게시글 내용");

        articleRepository.save(article);
        testEntityManager.flush();
        testEntityManager.clear();

        Article dbArticle = articleRepository.findById(article.getId()).orElseThrow();
        dbArticle.update("제목", "내용");
        testEntityManager.flush();
        testEntityManager.clear();

        // when
        Article result = articleRepository.findById(article.getId()).orElseThrow();

        // then
        assertThat(result.getId()).isEqualTo(article.getId());
        assertThat(result.getTitle()).isEqualTo("제목");
        assertThat(result.getContent()).isEqualTo("내용");
    }

    @Test
    @DisplayName("(물리 삭제) 생성된 게시글을 삭제하고 검증한다.")
    void delete() {
        // given
        Article article = toEntity("게시글 제목", "게시글 내용");
        articleRepository.save(article);

        // when
        articleRepository.delete(article);

        // then
        assertThat(articleRepository.existsById(article.getId())).isFalse();
    }

    @Test
    @DisplayName("게시글 리스트를 조회하고 검증한다.")
    void findAll() {
        // given
        Article article1 = toEntity("게시글 제목 1", "게시글 내용 1");
        Article article2 = toEntity("게시글 제목 2", "게시글 내용 2");
        Article article3 = toEntity("게시글 제목 3", "게시글 내용 3");
        articleRepository.saveAll(List.of(article1, article2, article3));

        // when
        List<Article> result = articleRepository.findAll();

        // then
        assertThat(result).hasSize(3)
                .extracting("title", "content")
                .containsExactly(
                        Tuple.tuple("게시글 제목 1", "게시글 내용 1"),
                        Tuple.tuple("게시글 제목 2", "게시글 내용 2"),
                        Tuple.tuple("게시글 제목 3", "게시글 내용 3")
                );
    }

    @Test
    @DisplayName("삭제되지 않은 게시글 리스트를 조회하고 검증한다.")
    void findAllByDeletedFalse() {
        // given
        Article article1 = toEntity("게시글 제목 1", "게시글 내용 1", false);
        Article article2 = toEntity("게시글 제목 2", "게시글 내용 2", false);
        Article article3 = toEntity("게시글 제목 3", "게시글 내용 3", true);
        Article article4 = toEntity("게시글 제목 4", "게시글 내용 4", false);
        articleRepository.saveAll(List.of(article1, article2, article3, article4));

        // when
        Page<Article> result = articleRepository.findAllByDeletedFalse(Pageable.unpaged(Sort.by(Sort.Direction.DESC, "id")));

        // then
        assertThat(result.getContent()).hasSize(3)
                .extracting("title", "content")
                .containsExactly(
                        Tuple.tuple("게시글 제목 4", "게시글 내용 4"),
                        Tuple.tuple("게시글 제목 2", "게시글 내용 2"),
                        Tuple.tuple("게시글 제목 1", "게시글 내용 1")
                );
    }

    @Test
    @DisplayName("페이징 처리된 게시글 리스트를 조회하고 검증한다.")
    void findAllByDeletedFalseWithPageable() {
        // given
        List<Article> articles = IntStream.range(1, 21)
                .mapToObj(i -> toEntity("게시글 제목 " + i, "게시글 내용 " + i, false))
                .collect(Collectors.toList());
        articleRepository.saveAll(articles);

        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "id");

        // when
        Page<Article> result = articleRepository.findAllByDeletedFalse(pageable);

        // then
        assertThat(result.getContent()).hasSize(5)
                .extracting("title", "content")
                .containsExactly(
                        Tuple.tuple("게시글 제목 20", "게시글 내용 20"),
                        Tuple.tuple("게시글 제목 19", "게시글 내용 19"),
                        Tuple.tuple("게시글 제목 18", "게시글 내용 18"),
                        Tuple.tuple("게시글 제목 17", "게시글 내용 17"),
                        Tuple.tuple("게시글 제목 16", "게시글 내용 16")
                );
    }

    @Test
    @DisplayName("페이징 처리된 게시글 리스트를 조회하지만 결과 값이 없다.")
    void findAllByDeletedFalseWithPageableSizeZero() {
        // when
        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "id");
        Page<Article> result = articleRepository.findAllByDeletedFalse(pageable);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("페이징 처리 매개변수를 갖는 메서드를 호출하지만 페이징 기능을 사용하지 않는다.")
    void findAllByDeletedFalseOrderByIdDescWithPageableNotUsed() {
        // given
        List<Article> articles = IntStream.range(1, 21)
                .mapToObj(i -> toEntity("게시글 제목 " + i, "게시글 내용 " + i, false))
                .collect(Collectors.toList());
        articleRepository.saveAll(articles);

        // when
        Page<Article> result = articleRepository.findAllByDeletedFalse(Pageable.unpaged());

        assertThat(result.getContent()).hasSize(20);
    }

    private Article toEntity(String title, String content, Boolean deleted) {
        return Article.builder()
                .member(member)
                .title(title)
                .content(content)
                .deleted(deleted)
                .build();
    }

    private Article toEntity(String title, String content) {
        return Article.builder()
                .member(member)
                .title(title)
                .content(content)
                .deleted(false)
                .build();
    }

}