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
import com.board.support.IntegrationTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static com.board.enumeration.ErrorCode.ARTICLE_NOT_FOUND;
import static com.board.enumeration.ErrorCode.COMMENT_NOT_FOUND;
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

}