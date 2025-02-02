package com.board.service.article.response;

import com.board.domain.article.Article;
import com.board.service.member.response.MemberResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleDetailResponse {

    private final Long id;
    private final String title;
    private final String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime createdDateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime modifiedDateTime;
    
    private final MemberResponse member;

    public static ArticleDetailResponse of(Article article) {
        return ArticleDetailResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .createdDateTime(article.getCreatedDateTime())
                .modifiedDateTime(article.getModifiedDateTime())
                .member(MemberResponse.of(article.getMember()))
                .build();
    }

}
