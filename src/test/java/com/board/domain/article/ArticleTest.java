package com.board.domain.article;

import com.board.domain.article.entity.Article;
import com.board.domain.member.entity.Member;
import com.board.global.common.exception.ConflictException;
import com.board.global.common.exception.ForbiddenException;
import com.board.global.common.exception.UnprocessableEntityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.board.global.common.enumeration.ErrorCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ArticleTest {

    private Member member;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .id(1L)
                .email("khghouse@daum.net")
                .password("Password12#$")
                .build();
    }

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
                .isInstanceOf(UnprocessableEntityException.class)
                .hasMessage(LENGTH_EXCEEDED.getMessage()); // 글자 수 제한을 초과하였습니다. [최대 {0}자]
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
                .isInstanceOf(UnprocessableEntityException.class)
                .hasMessage(LENGTH_EXCEEDED.getMessage()); // 글자 수 제한을 초과하였습니다. [최대 {0}자]
    }

    @Test
    @DisplayName("게시글 제목과 내용을 수정하고 확인한다.")
    void update() {
        // given
        Article article = toEntity(false);

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
        Article article = toEntity(false);

        // when
        article.delete();

        // then
        assertThat(article.getDeleted()).isTrue();
    }

    @Test
    @DisplayName("이미 삭제된 게시글이라 예외가 발생한다.")
    void deleteAlready() {
        // given
        Article article = toEntity(true);

        // when, then
        assertThatThrownBy(article::delete)
                .isInstanceOf(ConflictException.class)
                .hasMessage(ALREADY_DELETED.getMessage()); // 이미 삭제되었습니다.
    }

    @Test
    @DisplayName("요청 회원 ID가 실제 게시글의 작성자인지 확인한다.")
    void validateAuthor() {
        // given
        Article article = toEntity(false);

        // when, then
        article.validateWriter(1L);
    }

    @Test
    @DisplayName("요청 회원 ID가 실제 게시글의 작성자가 아니라면 예외가 발생한다.")
    void validateAuthorNotMatch() {
        // given
        Article article = toEntity(false);

        // when, then
        assertThatThrownBy(() -> article.validateWriter(2L))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage(INVALID_WRITER.getMessage()); // 작성자가 아닙니다.
    }

    @Test
    @DisplayName("조회수가 증가됨을 확인한다.")
    void incrementViewCount() {
        // given
        Article article = toEntity(false);

        // when
        article.incrementViewCount();

        // then
        assertThat(article.getViewCount()).isEqualTo(1);
    }

    private Article toEntityByTitle(String title) {
        return Article.builder()
                .title(title)
                .content("반갑습니다.")
                .build();
    }

    private Article toEntityByContent(String content) {
        return Article.builder()
                .title("안녕하세요.")
                .content(content)
                .build();
    }

    private Article toEntity(boolean deleted) {
        return Article.builder()
                .title("제목")
                .content("내용")
                .member(member)
                .deleted(deleted)
                .build();
    }

}