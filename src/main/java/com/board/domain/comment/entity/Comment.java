package com.board.domain.comment.entity;

import com.board.global.common.entity.BaseEntity;
import com.board.domain.article.entity.Article;
import com.board.domain.member.entity.Member;
import com.board.global.common.exception.BusinessException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.board.global.common.enumeration.ErrorCode.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(length = 300, nullable = false)
    private String content;

    private Boolean deleted;

    @Builder
    public Comment(Long id, Article article, Member member, String content, Boolean deleted) {
        this.id = id;
        this.article = article;
        this.member = member;
        this.content = validateContent(content);
        this.deleted = deleted;
    }

    public static Comment of(Article article, Member member, String content) {
        return Comment.builder()
                .article(article)
                .member(member)
                .content(content)
                .deleted(false)
                .build();
    }

    private String validateContent(String content) {
        int maxLength = 300;
        if (content.length() > maxLength) {
            throw new BusinessException(LENGTH_EXCEEDED, maxLength);
        }
        return content;
    }

    public void validateWriter(Long requestMemberId) {
        if (!this.getMember().getId().equals(requestMemberId)) {
            throw new BusinessException(INVALID_WRITER);
        }
    }

    public void update(String content) {
        this.content = validateContent(content);
    }

    public Long getArticleId() {
        return this.article.getId();
    }

    public void delete() {
        if (this.deleted) {
            throw new BusinessException(ALREADY_DELETED);
        }
        this.deleted = true;
    }

}
