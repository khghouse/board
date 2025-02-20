package com.board.service.comment;

import com.board.domain.article.Article;
import com.board.domain.article.ArticleRepository;
import com.board.domain.comment.Comment;
import com.board.domain.comment.CommentHierarchy;
import com.board.domain.comment.CommentHierarchyRepository;
import com.board.domain.comment.CommentRepository;
import com.board.domain.member.Member;
import com.board.domain.member.MemberRepository;
import com.board.service.comment.request.CommentHierarchyServiceRequest;
import com.board.support.IntegrationTestSupport;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class CommentHierarchySerivceTest extends IntegrationTestSupport {

    @Autowired
    private CommentHierarchySerivce commentHierarchySerivce;

    @Autowired
    private CommentHierarchyRepository commentHierarchyRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Article article;
    private Member member;
    private Comment comment;

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

        comment = Comment.builder()
                .article(article)
                .member(member)
                .content("댓글입니다.")
                .deleted(false)
                .build();
        commentRepository.save(comment);
    }

    @Test
    @DisplayName("댓글 계층 구조 데이터를 생성한다.")
    void createCommentHierarchy() {
        // given
        CommentHierarchyServiceRequest request = CommentHierarchyServiceRequest.withComment(comment);

        // when
        commentHierarchySerivce.createCommentHierarchy(request);

        // then
        List<CommentHierarchy> commentHierarchies = commentHierarchyRepository.findAllByDescendant(comment);
        assertThat(commentHierarchies).hasSize(1);
        assertThat(commentHierarchies.get(0)).extracting("descendant.content", "depth")
                .contains("댓글입니다.", 0);
    }

    @Test
    @DisplayName("댓글 계층 구조 데이터를 생성한다. [대댓글]")
    void createCommentHierarchy2() {
        // given
        Comment parentComment = Comment.builder()
                .article(article)
                .member(member)
                .content("부모 댓글입니다.")
                .deleted(false)
                .build();
        commentRepository.save(parentComment);
        commentHierarchyRepository.save(CommentHierarchy.of(parentComment, parentComment, 0));

        CommentHierarchyServiceRequest request = CommentHierarchyServiceRequest.of(comment, parentComment);

        // when
        commentHierarchySerivce.createCommentHierarchy(request);

        // then
        List<CommentHierarchy> commentHierarchies = commentHierarchyRepository.findAllByDescendant(comment);
        assertThat(commentHierarchies).hasSize(2);
        assertThat(commentHierarchies).extracting("ancestor.content", "depth")
                .containsExactly(
                        Tuple.tuple("댓글입니다.", 0),
                        Tuple.tuple("부모 댓글입니다.", 1)
                );
    }

    @Test
    @DisplayName("댓글 계층 구조 데이터를 생성한다. [대대댓글]")
    void createCommentHierarchy3() {
        // given
        Comment GrandParentComment = Comment.builder()
                .article(article)
                .member(member)
                .content("조부모 댓글입니다.")
                .deleted(false)
                .build();
        commentRepository.save(GrandParentComment);
        commentHierarchyRepository.save(CommentHierarchy.of(GrandParentComment, GrandParentComment, 0));

        Comment parentComment = Comment.builder()
                .article(article)
                .member(member)
                .content("부모 댓글입니다.")
                .deleted(false)
                .build();
        commentRepository.save(parentComment);
        commentHierarchyRepository.save(CommentHierarchy.of(parentComment, parentComment, 0));
        commentHierarchyRepository.save(CommentHierarchy.of(GrandParentComment, parentComment, 1));

        CommentHierarchyServiceRequest request = CommentHierarchyServiceRequest.of(comment, parentComment);

        // when
        commentHierarchySerivce.createCommentHierarchy(request);

        // then
        List<CommentHierarchy> commentHierarchies = commentHierarchyRepository.findAllByDescendant(comment);
        assertThat(commentHierarchies).hasSize(3);
        assertThat(commentHierarchies).extracting("ancestor.content", "depth")
                .containsExactly(
                        Tuple.tuple("댓글입니다.", 0),
                        Tuple.tuple("부모 댓글입니다.", 1),
                        Tuple.tuple("조부모 댓글입니다.", 2)
                );

        List<CommentHierarchy> allCommentHierarchies = commentHierarchyRepository.findAll();
        assertThat(allCommentHierarchies).hasSize(6);
    }

}