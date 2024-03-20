package com.board.domain.article;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ArticleTest {

    @Test
    @DisplayName("게시글 제목 글자수가 50자면 정상 처리된다.")
    void validateTitle() {
        // when
        Article article = toEntityByTitle("안녕하세요. 50자에 맞춰보겠습니다. 아직 부족한가요? 제목은 50자를 초과하며 안됩니다.");

        // then
        assertThat(article.getTitle()).isEqualTo("안녕하세요. 50자에 맞춰보겠습니다. 아직 부족한가요? 제목은 50자를 초과하며 안됩니다.");
    }

    @Test
    @DisplayName("게시글 제목을 50자 초과하여 예외가 발생한다.")
    void validateTitleOverLength() {
        // when, then
        assertThatThrownBy(() -> toEntityByTitle("안녕하세요. 50자를 한번 넘겨보겠습니다. 아직 부족한가요?? 제목은 50자를 초과하며 안됩니다."))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("게시글 제목은 50자를 초과할 수 없습니다.");
    }

    @Test
    @DisplayName("게시글 내용을 500자 초과하여 예외가 발생한다.")
    void postArticleExceedingContent() {
        // when, then
        assertThatThrownBy(() -> toEntityByContent("반갑습니다. 설마 내용을 500자 초과하려는 건가요? 이건 복붙을 참을 수 없습니다. 화이팅!! 화이팅!!!" +
                "설마 내용을 500자 초과하려는 건가요? 이건 복붙을 참을 수 없습니다. 화이팅!! 화이팅!!!" +
                "설마 내용을 500자 초과하려는 건가요? 이건 복붙을 참을 수 없습니다. 화이팅!! 화이팅!!!" +
                "설마 내용을 500자 초과하려는 건가요? 이건 복붙을 참을 수 없습니다. 화이팅!! 화이팅!!!" +
                "설마 내용을 500자 초과하려는 건가요? 이건 복붙을 참을 수 없습니다. 화이팅!! 화이팅!!!" +
                "설마 내용을 500자 초과하려는 건가요? 이건 복붙을 참을 수 없습니다. 화이팅!! 화이팅!!!" +
                "설마 내용을 500자 초과하려는 건가요? 이건 복붙을 참을 수 없습니다. 화이팅!! 화이팅!!!" +
                "설마 내용을 500자 초과하려는 건가요? 이건 복붙을 참을 수 없습니다. 화이팅!! 화이팅!!!" +
                "설마 내용을 500자 초과하려는 건가요? 이건 복붙을 참을 수 없습니다. 화이팅!! 화이팅!!!" +
                "설마 내용을 500자 초과하려는 건가요? 이건 복붙을 참을 수 없습니다. 화이팅!! 화이팅!!!"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("게시글 내용은 500자를 초과할 수 없습니다.");
    }

    private static Article toEntityByTitle(String title) {
        return Article.builder()
                .title(title)
                .content("반갑습니다.")
                .build();
    }

    private static Article toEntityByContent(String content) {
        return Article.builder()
                .title("안녕하세요.")
                .content(content)
                .build();
    }

}