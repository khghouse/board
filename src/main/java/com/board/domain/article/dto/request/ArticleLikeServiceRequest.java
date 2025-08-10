package com.board.domain.article.dto.request;

import com.board.domain.article.entity.Article;
import com.board.domain.article.entity.ArticleLike;
import com.board.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleLikeServiceRequest {

    private Long articleId;

    private Long memberId;

    @Builder(access = AccessLevel.PRIVATE)
    private ArticleLikeServiceRequest(Long articleId, Long memberId) {
        this.articleId = articleId;
        this.memberId = memberId;
    }

    public static ArticleLikeServiceRequest of(Long articleId, Long memberId) {
        return ArticleLikeServiceRequest.builder()
                .articleId(articleId)
                .memberId(memberId)
                .build();
    }

    public ArticleLike toEntity(Article article, Member member) {
        return ArticleLike.builder()
                .article(article)
                .member(member)
                .build();
    }

}
