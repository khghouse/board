package com.board.util;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Pageable;

public class QuerydslUtil {

    /**
     * 정렬 조건 동적 처리
     */
    public static <T> OrderSpecifier<?>[] createOrderSpecifiers(Pageable pageable, EntityPathBase<T> entityPath) {
        return pageable.getSort()
                .stream()
                .map(order -> {
                    PathBuilder<T> pathBuilder = new PathBuilder<>(entityPath.getType(), entityPath.getMetadata());
                    ComparableExpressionBase<?> fieldPath = pathBuilder.getComparable(order.getProperty(), Comparable.class);

                    return order.isAscending()
                            ? new OrderSpecifier<>(Order.ASC, fieldPath)
                            : new OrderSpecifier<>(Order.DESC, fieldPath);
                })
                .toArray(OrderSpecifier[]::new);
    }

    /**
     * 페이지 처리 적용
     */
    public static void applyPage(JPAQuery<?> query, Pageable pageable) {
        if (pageable.isPaged()) {
            query.offset(pageable.getOffset())
                    .limit(pageable.getPageSize());
        }
    }

}
