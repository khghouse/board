package com.board.support;

import com.board.configuration.QuerydslConfig;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Import(QuerydslConfig.class)
public abstract class RepositoryTestSupport {

    @Autowired
    protected TestEntityManager testEntityManager;

    @Autowired
    protected JPAQueryFactory queryFactory;

}
