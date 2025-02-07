package com.board.domain.article;

import com.board.domain.member.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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

}
