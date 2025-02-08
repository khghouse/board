package com.board.domain.article;

import com.board.domain.member.QMember;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ArticleQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final QArticle article = QArticle.article;
    private final QMember member = QMember.member;

    public List<Article> findAllWithMember() {
        return queryFactory.selectFrom(article)
                .innerJoin(article.member, member)
                .where(article.deleted.isFalse())
                .fetchJoin()
                .fetch();
    }

    public List<Article> findActiveArticles(Pageable pageable) {
        List<OrderSpecifier<?>> orderSpecifiers = getOrderSpecifiers(pageable, article);

        return queryFactory.selectFrom(article)
                .innerJoin(article.member, member)
                .where(article.deleted.isFalse())
                .orderBy(orderSpecifiers.get(0))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private List<OrderSpecifier<?>> getOrderSpecifiers(Pageable pageable, QArticle article) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        for (Sort.Order order : pageable.getSort()) {
            PathBuilder<Article> path = new PathBuilder<>(Article.class, "article");
            ComparableExpressionBase<?> fieldPath = path.getComparable(order.getProperty(), Comparable.class);

            // 오름차순 정렬
            if (order.getDirection().isAscending()) {
                orderSpecifiers.add(new OrderSpecifier<>(Order.ASC, fieldPath));
            }
            // 내림차순 정렬
            else {
                orderSpecifiers.add(new OrderSpecifier<>(Order.DESC, fieldPath));
            }
        }

        return orderSpecifiers;
    }

}
