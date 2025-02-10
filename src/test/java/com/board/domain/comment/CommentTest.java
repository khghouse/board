package com.board.domain.comment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommentTest {

    @Test
    @DisplayName("댓글 글자수가 300자면 정상 처리된다.")
    void validateContent() {
        // given
        String content = "댓글 글자 수는 300자를 넘길 수 없습니다. 댓글 글자 수는 300자를 넘길 수 없습니다. 댓글 글자 수는 300자를 넘길 수 없습니다. 댓글 글자 수는 300자를 넘길 수 없습니다. " +
                "댓글 글자 수는 300자를 넘길 수 없습니다. 댓글 글자 수는 300자를 넘길 수 없습니다. 댓글 글자 수는 300자를 넘길 수 없습니다. 댓글 글자 수는 300자를 넘길 수 없습니다." +
                "댓글 글자 수는 300자를 넘길 수 없습니다. 댓글 글자 수는 300자를 넘길 수 없습니다. 댓글 글자 수는 300자를 넘길 수 없습니다.";

        // when
        Comment comment = toEntityByContent(content);

        // then
        assertThat(comment.getContent().length()).isLessThanOrEqualTo(300);
    }

    @Test
    @DisplayName("댓글을 300자 초과하여 예외가 발생한다.")
    void validateContentOverLength() {
        // given
        String content = "댓글 글자 수는 300자를 넘길 수 없습니다. 댓글 글자 수는 300자를 넘길 수 없습니다. 댓글 글자 수는 300자를 넘길 수 없습니다. 댓글 글자 수는 300자를 넘길 수 없습니다. " +
                "댓글 글자 수는 300자를 넘길 수 없습니다. 댓글 글자 수는 300자를 넘길 수 없습니다. 댓글 글자 수는 300자를 넘길 수 없습니다. 댓글 글자 수는 300자를 넘길 수 없습니다." +
                "댓글 글자 수는 300자를 넘길 수 없습니다. 댓글 글자 수는 300자를 넘길 수 없습니다. 댓글 글자 수는 300자를 넘길 수 없습니다. 댓글 글자 수는 300자를 넘길 수 없습니다.";

        // when, then
        assertThatThrownBy(() -> toEntityByContent(content))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("댓글은 300자를 초과할 수 없습니다.");
    }

    private static Comment toEntityByContent(String content) {
        return Comment.builder()
                .content(content)
                .build();
    }

}