package com.board.domain.article;

import com.board.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {

    boolean existsByArticleAndMember(Article article, Member member);

    void deleteByArticleAndMember(Article article, Member member);

}
