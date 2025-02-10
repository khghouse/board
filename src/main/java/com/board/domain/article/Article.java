package com.board.domain.article;

import com.board.domain.BaseEntity;
import com.board.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(length = 50)
    private String title;

    @Column(length = 500)
    private String content;

    private Boolean deleted;

    @Builder
    private Article(Long id, Member member, String title, String content, Boolean deleted) {
        this.id = id;
        this.member = member;
        this.title = validateTitle(title);
        this.content = validateContent(content);
        this.deleted = deleted;
    }

    private String validateTitle(String title) {
        if (title.length() > 50) {
            throw new IllegalArgumentException("게시글 제목은 50자를 초과할 수 없습니다.");
        }
        return title;
    }

    private String validateContent(String content) {
        if (content.length() > 500) {
            throw new IllegalArgumentException("게시글 내용은 500자를 초과할 수 없습니다.");
        }
        return content;
    }

    public void update(String title, String content) {
        this.title = validateTitle(title);
        this.content = validateContent(content);
    }

    public void delete() {
        if (this.deleted) {
            throw new IllegalArgumentException("이미 삭제된 게시글입니다.");
        }
        this.deleted = true;
    }

    public void validateAuthor(Long requestMemberId) {
        if (!this.getMember().getId().equals(requestMemberId)) {
            throw new IllegalArgumentException("게시글 작성자가 아닙니다.");
        }
    }

}
