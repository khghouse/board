package com.board.domain.article;

import com.board.domain.RepositoryTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

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

}