package com.board.domain.article.repository;

import com.board.domain.article.entity.ArticleLike;
import com.board.domain.article.entity.QArticle;
import com.board.domain.article.entity.QArticleLike;
import com.board.domain.member.entity.QMember;
import com.board.global.common.util.QuerydslUtil;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ArticleLikeQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final QArticleLike articleLike = QArticleLike.articleLike;
    private final QMember member = QMember.member;
    private final QArticle article = QArticle.article;

    public Page<ArticleLike> findLikedMembers(Long articleId, Pageable pageable) {
        JPAQuery<ArticleLike> query = queryFactory.selectFrom(articleLike)
                .innerJoin(articleLike.member, member)
                .fetchJoin()
                .where(articleLike.article.id.eq(articleId)
                        .and(member.deleted.isFalse()))
                .orderBy(QuerydslUtil.createOrderSpecifiers(pageable, articleLike));
        QuerydslUtil.applyPage(query, pageable);
        List<ArticleLike> dataList = query.fetch();

        long count = pageable.isUnpaged() ? dataList.size() :
                Optional.ofNullable(queryFactory.select(articleLike.count())
                                .from(articleLike)
                                .innerJoin(articleLike.member, member)
                                .where(articleLike.article.id.eq(articleId)
                                        .and(member.deleted.isFalse()))
                                .fetchOne())
                        .orElse(0L);

        return new PageImpl<>(dataList, pageable, count);
    }

    public Page<ArticleLike> findLikedArticles(Long memberId, Pageable pageable) {
        JPAQuery<ArticleLike> query = queryFactory.selectFrom(articleLike)
                .innerJoin(articleLike.article, article)
                .fetchJoin()
                .where(articleLike.member.id.eq(memberId)
                        .and(article.deleted.isFalse()))
                .orderBy(QuerydslUtil.createOrderSpecifiers(pageable, articleLike));
        QuerydslUtil.applyPage(query, pageable);
        List<ArticleLike> dataList = query.fetch();

        long count = pageable.isUnpaged() ? dataList.size() :
                Optional.ofNullable(queryFactory.select(articleLike.count())
                                .from(articleLike)
                                .innerJoin(articleLike.article, article)
                                .where(articleLike.member.id.eq(memberId)
                                        .and(article.deleted.isFalse()))
                                .fetchOne())
                        .orElse(0L);

        return new PageImpl<>(dataList, pageable, count);
    }

}
