package com.board.domain.comment;

import com.board.domain.article.Article;
import com.board.domain.article.ArticleRepository;
import com.board.domain.member.Member;
import com.board.domain.member.MemberRepository;
import com.board.support.RepositoryTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommentRepositoryTest extends RepositoryTestSupport {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;
    private Article article;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .email("khghouse@daum.net")
                .password("Password12#$")
                .build();

        memberRepository.save(member);

        article = Article.builder()
                .title("안녕하세요.")
                .content("반갑습니다.")
                .deleted(false)
                .member(member)
                .build();

        articleRepository.save(article);
    }

    @Test
    @DisplayName("댓글을 등록, 조회하고 검증한다.")
    void saveAndFindById() {
        // given
        Comment comment = Comment.builder()
                .member(member)
                .article(article)
                .content("댓글입니다.")
                .deleted(false)
                .build();

        // when
        Comment result = commentRepository.save(comment);

        // then
        assertThat(result).extracting("id", "content", "article.title", "article.content", "member.email")
                .contains(comment.getId(), "댓글입니다.", "안녕하세요.", "반갑습니다.", "khghouse@daum.net");
    }

    @Test
    @DisplayName("댓글 1건의 조회 결과가 없어서 에러가 발생한다.")
    void notFindById() {
        // when, then
        assertThatThrownBy(() -> commentRepository.findById(1L).orElseThrow())
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("댓글을 수정하고 검증한다.")
    void update() {
        // given
        Comment comment = Comment.builder()
                .member(member)
                .article(article)
                .content("댓글입니다.")
                .deleted(false)
                .build();

        commentRepository.save(comment);
        testEntityManager.flush();
        testEntityManager.clear();

        Comment dbComment = commentRepository.findById(comment.getId()).orElseThrow();
        dbComment.update("댓글을 수정했습니다.");
        testEntityManager.flush();
        testEntityManager.clear();

        // when
        Comment result = commentRepository.findById(comment.getId()).orElseThrow();

        // then
        assertThat(result.getId()).isEqualTo(comment.getId());
        assertThat(result.getContent()).isEqualTo("댓글을 수정했습니다.");
    }

    @Test
    @DisplayName("(물리 삭제) 생성된 게시글을 삭제하고 검증한다.")
    void delete() {
        // given
        Comment comment = Comment.builder()
                .member(member)
                .article(article)
                .content("댓글입니다.")
                .deleted(false)
                .build();

        commentRepository.save(comment);

        // when
        commentRepository.delete(comment);

        // then
        assertThat(commentRepository.existsById(comment.getId())).isFalse();
    }

}