package com.board.service.article.request;

import com.board.domain.article.Article;
import com.board.domain.member.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleServiceRequest {

    private Long id;
    private String title;
    private String content;

    @SuppressWarnings("unused")
    @Builder(access = AccessLevel.PRIVATE)
    private ArticleServiceRequest(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public static ArticleServiceRequest of(Long id, String title, String content) {
        return ArticleServiceRequest.builder()
                .id(id)
                .title(title)
                .content(content)
                .build();
    }
    
    public static ArticleServiceRequest withId(Long id) {
        return ArticleServiceRequest.builder()
                .id(id)
                .build();
    }

    public static ArticleServiceRequest withTitleAndContent(String title, String content) {
        return ArticleServiceRequest.builder()
                .title(title)
                .content(content)
                .build();
    }

    public Article toEntity(Member member) {
        return Article.builder()
                .member(member)
                .title(title)
                .content(content)
                .deleted(false)
                .build();
    }

}
