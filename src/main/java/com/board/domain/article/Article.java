package com.board.domain.article;

import com.board.domain.BaseEntity;
import com.board.domain.member.Member;
import com.board.exception.BusinessException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.board.enumeration.ErrorCode.*;

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

    private int viewCount;

    @Builder
    private Article(Long id, Member member, String title, String content, Boolean deleted, int viewCount) {
        this.id = id;
        this.member = member;
        this.title = validateTitle(title);
        this.content = validateContent(content);
        this.deleted = deleted;
        this.viewCount = viewCount;
    }

    private String validateTitle(String title) {
        int maxLength = 50;
        if (title.length() > maxLength) {
            throw new BusinessException(LENGTH_EXCEEDED, maxLength);
        }
        return title;
    }

    private String validateContent(String content) {
        int maxLength = 500;
        if (content.length() > maxLength) {
            throw new BusinessException(LENGTH_EXCEEDED, maxLength);
        }
        return content;
    }

    public void update(String title, String content) {
        this.title = validateTitle(title);
        this.content = validateContent(content);
    }

    public void delete() {
        if (this.deleted) {
            throw new BusinessException(ALREADY_DELETED);
        }
        this.deleted = true;
    }

    public void validateWriter(Long requestMemberId) {
        if (!this.getMember().getId().equals(requestMemberId)) {
            throw new BusinessException(INVALID_WRITER);
        }
    }

    public void incrementViewCount() {
        this.viewCount++;
    }

}
