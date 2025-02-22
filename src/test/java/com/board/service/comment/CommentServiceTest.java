package com.board.service.comment;

import com.board.domain.article.Article;
import com.board.domain.article.ArticleRepository;
import com.board.domain.comment.Comment;
import com.board.domain.comment.CommentRepository;
import com.board.domain.member.Member;
import com.board.domain.member.MemberRepository;
import com.board.exception.BusinessException;
import com.board.service.comment.request.ChildCommentServiceRequest;
import com.board.service.comment.request.CommentServiceRequest;
import com.board.service.comment.response.CommentResponse;
import com.board.support.IntegrationTestSupport;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static com.board.enumeration.ErrorCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class CommentServiceTest extends IntegrationTestSupport {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager entityManager;

    private Article article;
    private Member member;

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
    @DisplayName("댓글을 등록하고 검증한다.")
    void createComment() {
        // given
        CommentServiceRequest request = CommentServiceRequest.withContentAndArticle("댓글입니다.", article.getId());

        // when
        commentService.createComment(request, member.getId());

        // then
        Comment result = commentRepository.findTopByOrderByIdDesc();
        assertThat(result.getContent()).isEqualTo("댓글입니다.");
        assertThat(result.getArticleId()).isEqualTo(article.getId());
        assertThat(result.getMember().getId()).isEqualTo(member.getId());
    }

    @Test
    @DisplayName("댓글을 등록할 때 게시글이 존재하지 않는다면 에러를 응답한다.")
    void createCommentNotExistArticle() {
        // given
        CommentServiceRequest request = CommentServiceRequest.withContentAndArticle("댓글입니다.", -1L);

        // when, then
        assertThatThrownBy(() -> commentService.createComment(request, member.getId()))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ARTICLE_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("대댓글을 등록하고 검증한다.")
    void createChildComment() {
        // given
        Comment parentComment = Comment.builder()
                .article(article)
                .member(member)
                .content("댓글입니다.")
                .deleted(false)
                .build();
        commentRepository.save(parentComment);

        ChildCommentServiceRequest request = ChildCommentServiceRequest.of(parentComment.getId(), "대댓글입니다.");

        // when
        commentService.createChildComment(request, member.getId());

        // then
        Comment result = commentRepository.findTopByOrderByIdDesc();
        assertThat(result.getContent()).isEqualTo("대댓글입니다.");
        assertThat(result.getArticleId()).isEqualTo(article.getId());
        assertThat(result.getMember().getId()).isEqualTo(member.getId());
    }

    @Test
    @DisplayName("대댓글을 등록할 때 게시글이 존재하지 않는다면 에러를 응답한다.")
    void createChildCommentNotExistArticle() {
        // given
        Article article = Article.builder()
                .title("안녕하세요.")
                .content("반갑습니다.")
                .deleted(true)
                .member(member)
                .build();
        articleRepository.save(article);

        Comment parentComment = Comment.builder()
                .article(article)
                .member(member)
                .content("댓글입니다.")
                .deleted(false)
                .build();
        commentRepository.save(parentComment);

        ChildCommentServiceRequest request = ChildCommentServiceRequest.of(parentComment.getId(), "대댓글입니다.");

        // when, then
        assertThatThrownBy(() -> commentService.createChildComment(request, member.getId()))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ARTICLE_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("대댓글을 등록할 때 게시글이 존재하지 않는다면 에러를 응답한다.")
    void createChildCommentNotExistParentComment() {
        // given
        ChildCommentServiceRequest request = ChildCommentServiceRequest.of(-1L, "대댓글입니다.");

        // when, then
        assertThatThrownBy(() -> commentService.createChildComment(request, member.getId()))
                .isInstanceOf(BusinessException.class)
                .hasMessage(COMMENT_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("댓글 1건을 조회하고 검증한다.")
    void getComment() {
        // given
        Comment comment = Comment.builder()
                .article(article)
                .member(member)
                .content("댓글입니다.")
                .deleted(false)
                .build();
        commentRepository.save(comment);

        // when
        CommentResponse result = commentService.getComment(comment.getId());

        // then
        assertThat(result.getId()).isEqualTo(comment.getId());
        assertThat(result.getContent()).isEqualTo(comment.getContent());
        assertThat(result.getMember().getEmail()).isEqualTo("khghouse@daum.net");
    }

    @Test
    @DisplayName("댓글 1건의 조회 결과가 없어서 예외가 발생한다.")
    void getCommentNotFound() {
        // when, then
        assertThatThrownBy(() -> commentService.getComment(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessage(COMMENT_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("댓글을 수정하고 검증한다.")
    void updateComment() {
        // given
        Comment comment = Comment.builder()
                .article(article)
                .member(member)
                .content("댓글입니다.")
                .deleted(false)
                .build();
        commentRepository.save(comment);
        entityManager.flush();
        entityManager.clear();

        CommentServiceRequest request = CommentServiceRequest.withIdAndContent(comment.getId(), "댓글 수정");

        // when
        commentService.updateComment(request, member.getId());
        entityManager.flush();
        entityManager.clear();

        // then
        Comment result = commentRepository.findById(comment.getId()).orElseThrow();

        assertThat(result.getContent()).isEqualTo("댓글 수정");
    }

    @Test
    @DisplayName("수정하려는 댓글 정보가 없어서 예외가 발생한다.")
    void updateCommentNotFound() {
        // given
        CommentServiceRequest request = CommentServiceRequest.withIdAndContent(1L, "수정할 댓글이 없음");

        // when, then1
        assertThatThrownBy(() -> commentService.updateComment(request, 1L))
                .isInstanceOf(BusinessException.class)
                .hasMessage(COMMENT_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("본인이 작성하지 않은 댓글을 수정하려고 한다면 예외가 발생한다.")
    void updateCommentInvalidWriter() {
        // given
        Comment comment = Comment.builder()
                .article(article)
                .member(member)
                .content("댓글입니다.")
                .deleted(false)
                .build();
        commentRepository.save(comment);

        CommentServiceRequest request = CommentServiceRequest.withIdAndContent(comment.getId(), "댓글 수정");

        // when, then
        assertThatThrownBy(() -> commentService.updateComment(request, 2L))
                .isInstanceOf(BusinessException.class)
                .hasMessage(INVALID_WRITER.getMessage());
    }

    @Test
    @DisplayName("등록된 댓글을 삭제하고 검증한다.")
    void deleteComment() {
        // given
        Comment comment = Comment.builder()
                .article(article)
                .member(member)
                .content("댓글입니다.")
                .deleted(false)
                .build();
        commentRepository.save(comment);

        // when
        commentService.deleteComment(comment.getId(), member.getId());

        // then
        Comment result = commentRepository.findById(comment.getId()).orElseThrow();
        assertThat(result.getDeleted()).isTrue();
    }

    @Test
    @DisplayName("등록된 댓글을 삭제하려고 했는데 이미 삭제된 댓글이라 예외가 발생한다.")
    void deleteCommentAlreadyDeleted() {
        // given
        Comment comment = Comment.builder()
                .article(article)
                .member(member)
                .content("댓글입니다.")
                .deleted(true)
                .build();
        commentRepository.save(comment);

        // when, then
        assertThatThrownBy(() -> commentService.deleteComment(comment.getId(), member.getId()))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ALREADY_DELETED.getMessage());
    }

    @Test
    @DisplayName("삭제하려는 댓글 정보가 없어서 예외가 발생한다.")
    void deleteCommentNotFound() {
        // when, then
        assertThatThrownBy(() -> commentService.deleteComment(1L, 1L))
                .isInstanceOf(BusinessException.class)
                .hasMessage(COMMENT_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("본인이 작성하지 않은 댓글삭제 하려고 한다면 예외가 발생한다.")
    void deleteCommentInvalidWriter() {
        // given
        Comment comment = Comment.builder()
                .article(article)
                .member(member)
                .content("댓글입니다.")
                .deleted(false)
                .build();
        commentRepository.save(comment);

        // when, then
        assertThatThrownBy(() -> commentService.deleteComment(comment.getId(), 2L))
                .isInstanceOf(BusinessException.class)
                .hasMessage(INVALID_WRITER.getMessage());
    }

}