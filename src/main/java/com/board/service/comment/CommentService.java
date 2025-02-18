package com.board.service.comment;


import com.board.domain.article.Article;
import com.board.domain.article.ArticleRepository;
import com.board.domain.comment.Comment;
import com.board.domain.comment.CommentRepository;
import com.board.domain.member.Member;
import com.board.domain.member.MemberRepository;
import com.board.exception.BusinessException;
import com.board.service.comment.request.ChildCommentServiceRequest;
import com.board.service.comment.request.CommentHierarchyServiceRequest;
import com.board.service.comment.request.CommentServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.board.enumeration.ErrorCode.ARTICLE_NOT_FOUND;
import static com.board.enumeration.ErrorCode.COMMENT_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentHierarchySerivce commentHierarchySerivce;

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

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

    @Transactional
    public void updateComment(CommentServiceRequest request, Long memberId) {
        Comment comment = findValidComment(request.getId());
        comment.validateWriter(memberId);
        comment.update(request.getContent());
    }

    private Comment saveComment(String content, Long memberId, Long articleId) {
        Article article = articleRepository.findByIdAndDeletedFalse(articleId)
                .orElseThrow(() -> new BusinessException(ARTICLE_NOT_FOUND));

        Member memberProxy = memberRepository.getReferenceById(memberId);

        return commentRepository.save(Comment.of(article, memberProxy, content));
    }

    private Comment findValidComment(Long commentId) {
        return commentRepository.findByIdAndDeletedFalse(commentId)
                .orElseThrow(() -> new BusinessException(COMMENT_NOT_FOUND));
    }

}
