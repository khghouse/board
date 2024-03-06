package com.board.domain.article;

import com.board.domain.RepositoryTestSupport;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ArticleRepositoryTest extends RepositoryTestSupport {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @DisplayName("게시글을 등록하고 검증한다.")
    void save() {
        // given
        Article article = toEntity("게시글 제목", "게시글 내용");

        // when
        Article result = articleRepository.save(article);

        // then
        assertThat(result).extracting("title", "content")
                .contains("게시글 제목", "게시글 내용");
    }

    @Test
    @DisplayName("게시글 1건을 조회하고 검증한다.")
    void findById() {
        // given
        Article article = toEntity("게시글 제목", "게시글 내용");

        articleRepository.save(article);

        // when
        Article result = articleRepository.findById(article.getId())
                .get();

        // then
        assertThat(result.getId()).isEqualTo(article.getId());
        assertThat(result.getTitle()).isEqualTo("게시글 제목");
        assertThat(result.getContent()).isEqualTo("게시글 내용");
    }

    @Test
    @DisplayName("게시글 1건의 조회 결과가 없어서 에러가 발생한다.")
    void notFindById() {
        // when, then
        assertThatThrownBy(() -> articleRepository.findById(1L).get())
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("게시글을 수정하고 검증한다.")
    void update() {
        // given
        Article article = toEntity("게시글 제목", "게시글 내용");

        articleRepository.save(article);
        Article dbArticle = testEntityManager.find(Article.class, article.getId());
        dbArticle.update("제목", "내용");
        testEntityManager.flush();

        // when
        Article result = articleRepository.findById(article.getId())
                .get();

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
    void findAllByDeletedFalseOrderByIdDesc() {
        // given
        Article article1 = toEntity("게시글 제목 1", "게시글 내용 1", false);
        Article article2 = toEntity("게시글 제목 2", "게시글 내용 2", false);
        Article article3 = toEntity("게시글 제목 3", "게시글 내용 3", true);
        Article article4 = toEntity("게시글 제목 4", "게시글 내용 4", false);
        articleRepository.saveAll(List.of(article1, article2, article3, article4));

        // when
        List<Article> result = articleRepository.findAllByDeletedFalseOrderByIdDesc();

        // then
        assertThat(result).hasSize(3)
                .extracting("title", "content")
                .containsExactly(
                        Tuple.tuple("게시글 제목 4", "게시글 내용 4"),
                        Tuple.tuple("게시글 제목 2", "게시글 내용 2"),
                        Tuple.tuple("게시글 제목 1", "게시글 내용 1")
                );
    }

    private static Article toEntity(String title, String content, Boolean deleted) {
        return Article.builder()
                .title(title)
                .content(content)
                .deleted(deleted)
                .build();
    }

    private static Article toEntity(String title, String content) {
        return Article.builder()
                .title(title)
                .content(content)
                .deleted(false)
                .build();
    }

}