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
public class ArticleQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final QArticle article = QArticle.article;
    private final QMember member = QMember.member;

    public Page<Article> findActiveArticles(Pageable pageable) {
        JPAQuery<Article> query = queryFactory.selectFrom(article)
                .innerJoin(article.member, member)
                .fetchJoin()
                .where(article.deleted.isFalse())
                .orderBy(QuerydslUtil.createOrderSpecifiers(pageable, article));
        QuerydslUtil.applyPage(query, pageable);
        List<Article> articles = query.fetch();

        long count = pageable.isUnpaged() ? articles.size() :
                Optional.ofNullable(queryFactory.select(article.count())
                                .from(article)
                                .where(article.deleted.isFalse())
                                .fetchOne())
                        .orElse(0L);

        return new PageImpl<>(articles, pageable, count);
    }

}
