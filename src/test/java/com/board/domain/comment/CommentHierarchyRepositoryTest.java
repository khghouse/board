package com.board.domain.comment;

import com.board.domain.article.entity.Article;
import com.board.domain.article.repository.ArticleRepository;
import com.board.domain.comment.entity.Comment;
import com.board.domain.comment.entity.CommentHierarchy;
import com.board.domain.comment.repository.CommentHierarchyRepository;
import com.board.domain.comment.repository.CommentRepository;
import com.board.domain.member.entity.Member;
import com.board.domain.member.repository.MemberRepository;
import com.board.support.RepositoryTestSupport;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CommentHierarchyRepositoryTest extends RepositoryTestSupport {

    @Autowired
    private CommentHierarchyRepository commentHierarchyRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;
    private Article article;
    private Comment rootComment;

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

        rootComment = Comment.builder()
                .member(member)
                .article(article)
                .content("댓글입니다.")
                .deleted(false)
                .build();

        commentRepository.save(rootComment);
    }

    @Test
    @DisplayName("댓글 저장 시, 클로저 테이블에 관계를 저장한다.")
    void saveRootCommentHierarchy() {
        // given
        CommentHierarchy commentHierarchy = CommentHierarchy.builder()
                .ancestor(rootComment)
                .descendant(rootComment)
                .depth(0)
                .build();

        // when
        CommentHierarchy result = commentHierarchyRepository.save(commentHierarchy);

        // then
        assertThat(result).extracting("id", "ancestor.id", "descendant.id", "depth")
                .contains(rootComment.getId(), rootComment.getId(), rootComment.getId(), 0);

    }

    @Test
    @DisplayName("대댓글 저장 시, 클로저 테이블에 관계를 저장한다.")
    void saveDescendantCommentHierarchy() {
        // given
        CommentHierarchy commentHierarchy = CommentHierarchy.builder()
                .ancestor(rootComment)
                .descendant(rootComment)
                .depth(0)
                .build();

        commentHierarchyRepository.save(commentHierarchy);

        Comment comment = Comment.builder()
                .member(member)
                .article(article)
                .content("대댓글입니다.")
                .deleted(false)
                .build();

        commentRepository.save(comment);

        CommentHierarchy commentHierarchy2 = CommentHierarchy.builder()
                .ancestor(comment)
                .descendant(comment)
                .depth(0)
                .build();

        CommentHierarchy commentHierarchy3 = CommentHierarchy.builder()
                .ancestor(rootComment)
                .descendant(comment)
                .depth(1)
                .build();

        // when
        commentHierarchyRepository.saveAll(List.of(commentHierarchy2, commentHierarchy3));

        // then
        List<CommentHierarchy> commentHierarchies = commentHierarchyRepository.findAllByDescendant(comment);

        assertThat(commentHierarchies).extracting("ancestor.id", "descendant.id", "depth")
                .containsExactly(
                        Tuple.tuple(comment.getId(), comment.getId(), 0),
                        Tuple.tuple(rootComment.getId(), comment.getId(), 1)
                );

    }

}