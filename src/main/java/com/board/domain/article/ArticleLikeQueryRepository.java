package com.board.domain.article;

import com.board.domain.member.QMember;
import com.board.util.QuerydslUtil;
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
                                .where(member.deleted.isFalse())
                                .fetchOne())
                        .orElse(0L);

        return new PageImpl<>(dataList, pageable, count);
    }

}
