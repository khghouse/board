package com.board.domain.article;

import com.board.domain.member.Member;
import com.board.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.board.enumeration.ErrorCode.*;
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
                .isInstanceOf(BusinessException.class)
                .hasMessage(String.format("%s [최대 %d자]", LENGTH_EXCEEDED.getMessage(), 50));
    }

    @Test
    @DisplayName("게시글 내용을 500자 초과하여 예외가 발생한다.")
    void validateContentOverLength() {
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
                .isInstanceOf(BusinessException.class)
                .hasMessage(String.format("%s [최대 %d자]", LENGTH_EXCEEDED.getMessage(), 500));
    }

    @Test
    @DisplayName("게시글 제목과 내용을 수정하고 확인한다.")
    void update() {
        // given
        Article article = Article.builder()
                .title("제목")
                .content("내용")
                .build();

        // when
        article.update("안녕하세요.", "반갑습니다.");

        // then
        assertThat(article).extracting("title", "content")
                .contains("안녕하세요.", "반갑습니다.");
    }

    @Test
    @DisplayName("게시글을 삭제하고 확인한다.")
    void delete() {
        // given
        Article article = Article.builder()
                .title("제목")
                .content("내용")
                .deleted(false)
                .build();

        // when
        article.delete();

        // then
        assertThat(article.getDeleted()).isTrue();
    }

    @Test
    @DisplayName("이미 삭제된 게시글이라 예외가 발생한다.")
    void deleteAlready() {
        // given
        Article article = Article.builder()
                .title("제목")
                .content("내용")
                .deleted(true)
                .build();

        // when, then
        assertThatThrownBy(article::delete)
                .isInstanceOf(BusinessException.class)
                .hasMessage(ALREADY_DELETED.getMessage());
    }

    @Test
    @DisplayName("요청 회원 ID가 실제 게시글의 작성자인지 확인한다.")
    void validateAuthor() {
        // given
        Member member = Member.builder()
                .id(1L)
                .email("khghouse@daum.net")
                .password("Password12#$")
                .build();

        Article article = Article.builder()
                .title("제목")
                .content("내용")
                .member(member)
                .build();

        // when, then
        article.validateWriter(1L);
    }

    @Test
    @DisplayName("요청 회원 ID가 실제 게시글의 작성자가 아니라면 예외가 발생한다.")
    void validateAuthorNotMatch() {
        // given
        Member member = Member.builder()
                .id(1L)
                .email("khghouse@daum.net")
                .password("Password12#$")
                .build();

        Article article = Article.builder()
                .title("제목")
                .content("내용")
                .member(member)
                .build();

        // when, then
        assertThatThrownBy(() -> article.validateWriter(2L))
                .isInstanceOf(BusinessException.class)
                .hasMessage(INVALID_WRITER.getMessage());
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