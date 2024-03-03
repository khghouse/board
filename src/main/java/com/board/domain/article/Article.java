package com.board.domain.article;

import com.board.domain.BaseEntity;
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

    @Column(length = 50)
    private String title;

    @Column(length = 500)
    private String content;

    @Builder
    private Article(Long id, String title, String content) {
        this.id = id;
        this.title = validateTitle(title);
        this.content = validateContent(content);
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
}
