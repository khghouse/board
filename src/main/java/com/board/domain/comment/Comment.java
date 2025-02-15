package com.board.domain.comment;

import com.board.domain.BaseEntity;
import com.board.domain.article.Article;
import com.board.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
        if (content.length() > 300) {
            throw new IllegalArgumentException("댓글은 300자를 초과할 수 없습니다.");
        }
        return content;
    }

    public void update(String content) {
        this.content = validateContent(content);
    }

    public Long getArticleId() {
        return this.article.getId();
    }

}
