package com.board.domain.article;

import com.board.domain.RepositoryTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ArticleRepositoryTest extends RepositoryTestSupport {

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    @DisplayName("게시글을 등록하고 검증한다.")
    void save() {
        // given
        Article article = Article.builder()
                .title("게시글 제목")
                .content("게시글 내용")
                .build();

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
        Article article = Article.builder()
                .title("게시글 제목")
                .content("게시글 내용")
                .build();

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

}