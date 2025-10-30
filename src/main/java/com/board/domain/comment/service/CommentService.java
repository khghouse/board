package com.board.domain.comment.service;


import com.board.domain.article.entity.Article;
import com.board.domain.article.repository.ArticleRepository;
import com.board.domain.comment.dto.request.ChildCommentServiceRequest;
import com.board.domain.comment.dto.request.CommentHierarchyServiceRequest;
import com.board.domain.comment.dto.request.CommentServiceRequest;
import com.board.domain.comment.dto.response.CommentResponse;
import com.board.domain.comment.entity.Comment;
import com.board.domain.comment.repository.CommentRepository;
import com.board.domain.member.entity.Member;
import com.board.domain.member.service.MemberService;
import com.board.global.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.board.global.common.enumeration.ErrorCode.ARTICLE_NOT_FOUND;
import static com.board.global.common.enumeration.ErrorCode.COMMENT_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentHierarchySerivce commentHierarchySerivce;
    private final MemberService memberService;

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;

    /**
     * 일반 댓글(루트 댓글)인 경우 댓글 등록
     */
    @Transactional
    public void createComment(CommentServiceRequest request, Long memberId) {
        Comment comment = saveComment(request.getContent(), memberId, request.getArticleId());
        commentHierarchySerivce.createCommentHierarchy(CommentHierarchyServiceRequest.withComment(comment));
    }

    /**
     * 대댓글 인 경우 댓글 등록
     */
    @Transactional
    public void createChildComment(ChildCommentServiceRequest request, Long memberId) {
        Comment parentComment = findValidComment(request.getParentCommentId());
        Comment comment = saveComment(request.getContent(), memberId, parentComment.getArticleId());
        commentHierarchySerivce.createCommentHierarchy(CommentHierarchyServiceRequest.of(comment, parentComment));
    }

    public CommentResponse getComment(Long id) {
        Comment comment = findValidComment(id);
        return CommentResponse.of(comment);
    }

    @Transactional
    public void updateComment(CommentServiceRequest request, Long memberId) {
        Comment comment = findValidComment(request.getId());
        comment.validateWriter(memberId);
        comment.update(request.getContent());
    }

    @Transactional
    public void deleteComment(Long commentId, Long memberId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(COMMENT_NOT_FOUND));
        comment.validateWriter(memberId);
        comment.delete();
    }

    private Comment saveComment(String content, Long memberId, Long articleId) {
        Article article = articleRepository.findByIdAndDeletedFalse(articleId)
                .orElseThrow(() -> new NotFoundException(ARTICLE_NOT_FOUND));

        Member member = memberService.getMemberById(memberId);

        return commentRepository.save(Comment.of(article, member, content));
    }

    private Comment findValidComment(Long commentId) {
        return commentRepository.findByIdAndDeletedFalse(commentId)
                .orElseThrow(() -> new NotFoundException(COMMENT_NOT_FOUND));
    }

}
