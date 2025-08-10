package com.board.domain.article.repository;

import com.board.domain.article.entity.Article;
import com.board.domain.article.entity.ArticleLike;
import com.board.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {

    boolean existsByArticleAndMember(Article article, Member member);

    void deleteByArticleAndMember(Article article, Member member);

}
