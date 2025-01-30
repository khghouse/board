package com.board.service.article;

import com.board.domain.article.Article;
import com.board.domain.article.ArticleRepository;
import com.board.domain.member.Member;
import com.board.domain.member.MemberRepository;
import com.board.dto.page.PageResponse;
import com.board.dto.page.PageServiceRequest;
import com.board.exception.BusinessException;
import com.board.service.article.request.ArticleServiceRequest;
import com.board.service.article.response.ArticleResponse;
import com.board.support.IntegrationTestSupport;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class ArticleServiceTest extends IntegrationTestSupport {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("게시글을 등록하고 검증한다.")
    void createArticle() {
        // given
        Member member = Member.builder()
                .email("khghouse@daum.net")
                .password("Password12#$")
                .build();

        memberRepository.save(member);

        ArticleServiceRequest request = ArticleServiceRequest.of("안녕하세요.", "반갑습니다.");

        // when
        ArticleResponse result = articleService.createArticle(request, member.getId());

        // then
        assertThat(result).extracting("title", "content")
                .contains("안녕하세요.", "반갑습니다.");
    }

    @Test
    @DisplayName("게시글 1건을 조회하고 검증한다.")
    void getArticle() {
        // given
        Article article = toEntity("게시글 제목", "게시글 내용");
        articleRepository.save(article);

        // when
        ArticleResponse result = articleService.getArticle(article.getId());

        // then
        assertThat(result.getId()).isEqualTo(article.getId());
        assertThat(result.getTitle()).isEqualTo("게시글 제목");
        assertThat(result.getContent()).isEqualTo("게시글 내용");
    }

    @Test
    @DisplayName("게시글 1건의 조회 결과가 없어서 예외가 발생한다.")
    void getArticleNotFind() {
        // when, then
        assertThatThrownBy(() -> articleService.getArticle(1L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("게시글 정보가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("게시글을 수정하고 검증한다.")
    void updateArticle() {
        // given
        Article article = toEntity("게시글 제목", "게시글 내용");
        articleRepository.save(article);

        ArticleServiceRequest request = ArticleServiceRequest.of(article.getId(), "안녕하세요.", "반갑습니다.");

        // when
        ArticleResponse result = articleService.updateArticle(request);

        // then
        assertThat(result).extracting("title", "content")
                .contains("안녕하세요.", "반갑습니다.");
    }

    @Test
    @DisplayName("수정하려는 게시글 정보가 없어서 예외가 발생한다.")
    void updateArticleNotFind() {
        // given
        ArticleServiceRequest request = ArticleServiceRequest.of(1L);

        // when, then
        assertThatThrownBy(() -> articleService.updateArticle(request))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("게시글 정보가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("등록된 게시글을 삭제하고 검증한다.")
    void deleteArticle() {
        // given
        Article article = toEntity("게시글 제목", "게시글 내용", false);
        articleRepository.save(article);

        // when
        articleService.deleteArticle(article.getId());

        // then
        Article result = articleRepository.findById(article.getId()).get();
        assertThat(result.getDeleted()).isTrue();
    }

    @Test
    @DisplayName("등록된 게시글을 삭제하려고 했는데 이미 삭제된 게시글이라 예외가 발생한다.")
    void deleteArticleAlreadyDeleted() {
        // given
        Article article = toEntity("게시글 제목", "게시글 내용", true);
        articleRepository.save(article);

        // when, then
        assertThatThrownBy(() -> articleService.deleteArticle(article.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 삭제된 게시글입니다.");
    }

    @Test
    @DisplayName("삭제하려는 게시글 정보가 없어서 예외가 발생한다.")
    void deleteArticleNotFind() {
        // when, then
        assertThatThrownBy(() -> articleService.deleteArticle(1L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("게시글 정보가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("게시글 리스트를 조회하고 검증한다.")
    void getArticleList() {
        // given
        Article article1 = toEntity("게시글 제목 1", "게시글 내용 1", false);
        Article article2 = toEntity("게시글 제목 2", "게시글 내용 2", true);
        Article article3 = toEntity("게시글 제목 3", "게시글 내용 3", false);
        Article article4 = toEntity("게시글 제목 4", "게시글 내용 4", false);
        articleRepository.saveAll(List.of(article1, article2, article3, article4));

        PageServiceRequest request = PageServiceRequest.builder()
                .build();

        // when
        PageResponse result = articleService.getArticleList(request);

        // then
        assertThat(result.getPageInfomation().getPageNumber()).isEqualTo(1);
        assertThat(result.getPageInfomation().getTotalPages()).isEqualTo(1);
        assertThat(result.getPageInfomation().getTotalElements()).isEqualTo(3);
        assertThat(result.getPageInfomation().getIsLast()).isTrue();
        assertThat(result.getContents()).hasSize(3);
    }

    @Test
    @DisplayName("게시글 리스트 사이즈가 0이면 빈 배열을 응답한다.")
    void getArticleListSizeZero() {
        // given
        PageServiceRequest request = PageServiceRequest.builder()
                .build();

        // when
        PageResponse result = articleService.getArticleList(request);

        // then
        assertThat(result.getPageInfomation().getPageNumber()).isEqualTo(1);
        assertThat(result.getPageInfomation().getTotalPages()).isEqualTo(1);
        assertThat(result.getPageInfomation().getTotalElements()).isEqualTo(0);
        assertThat(result.getPageInfomation().getIsLast()).isTrue();
        assertThat(result.getContents()).hasSize(0);
    }

    @Test
    @DisplayName("페이징 처리된 게시글 리스트를 조회하고 검증한다.")
    void getArticleListPageable() {
        // given
        List<Article> articles = IntStream.range(1, 21)
                .mapToObj(i -> toEntity("게시글 제목 " + i, "게시글 내용 " + i, false))
                .collect(Collectors.toList());
        articleRepository.saveAll(articles);

        PageServiceRequest request = PageServiceRequest.builder()
                .pageNumber(1)
                .pageSize(5)
                .direction("desc")
                .property("id")
                .build();

        // when
        PageResponse result = articleService.getArticleList(request);

        // then
        assertThat(result.getPageInfomation().getPageNumber()).isEqualTo(1);
        assertThat(result.getPageInfomation().getTotalPages()).isEqualTo(4);
        assertThat(result.getPageInfomation().getTotalElements()).isEqualTo(20);
        assertThat(result.getPageInfomation().getIsLast()).isFalse();
        assertThat(result.getContents()).hasSize(5)
                .extracting("title", "content")
                .containsExactly(
                        Tuple.tuple("게시글 제목 20", "게시글 내용 20"),
                        Tuple.tuple("게시글 제목 19", "게시글 내용 19"),
                        Tuple.tuple("게시글 제목 18", "게시글 내용 18"),
                        Tuple.tuple("게시글 제목 17", "게시글 내용 17"),
                        Tuple.tuple("게시글 제목 16", "게시글 내용 16")
                );
    }

    @Test
    @DisplayName("페이징 처리된 게시글 리스트의 마지막 페이지 조회하고 검증한다.")
    void getArticleListPageableLastPage() {
        // given
        List<Article> articles = IntStream.range(1, 21)
                .mapToObj(i -> toEntity("게시글 제목 " + i, "게시글 내용 " + i, false))
                .collect(Collectors.toList());
        articleRepository.saveAll(articles);

        PageServiceRequest request = PageServiceRequest.builder()
                .pageNumber(5)
                .pageSize(4)
                .direction("desc")
                .property("id")
                .build();

        // when
        PageResponse result = articleService.getArticleList(request);

        // then
        assertThat(result.getPageInfomation().getPageNumber()).isEqualTo(5);
        assertThat(result.getPageInfomation().getTotalPages()).isEqualTo(5);
        assertThat(result.getPageInfomation().getTotalElements()).isEqualTo(20);
        assertThat(result.getPageInfomation().getIsLast()).isTrue();
        assertThat(result.getContents()).hasSize(4)
                .extracting("title", "content")
                .containsExactly(
                        Tuple.tuple("게시글 제목 4", "게시글 내용 4"),
                        Tuple.tuple("게시글 제목 3", "게시글 내용 3"),
                        Tuple.tuple("게시글 제목 2", "게시글 내용 2"),
                        Tuple.tuple("게시글 제목 1", "게시글 내용 1")
                );
    }

    @Test
    @DisplayName("ID 오름차순 정렬된 게시글 리스트의 조회하고 검증한다.")
    void getArticleListPageablOrderByIdAsc() {
        // given
        List<Article> articles = IntStream.range(1, 21)
                .mapToObj(i -> toEntity("게시글 제목 " + i, "게시글 내용 " + i, false))
                .collect(Collectors.toList());
        articleRepository.saveAll(articles);

        PageServiceRequest request = PageServiceRequest.builder()
                .pageNumber(2)
                .pageSize(10)
                .direction("asc")
                .build();

        // when
        PageResponse result = articleService.getArticleList(request);

        // then
        assertThat(result.getPageInfomation().getPageNumber()).isEqualTo(2);
        assertThat(result.getPageInfomation().getTotalPages()).isEqualTo(2);
        assertThat(result.getPageInfomation().getTotalElements()).isEqualTo(20);
        assertThat(result.getPageInfomation().getIsLast()).isTrue();
        assertThat(result.getContents()).hasSize(10)
                .extracting("title", "content")
                .containsExactly(
                        Tuple.tuple("게시글 제목 11", "게시글 내용 11"),
                        Tuple.tuple("게시글 제목 12", "게시글 내용 12"),
                        Tuple.tuple("게시글 제목 13", "게시글 내용 13"),
                        Tuple.tuple("게시글 제목 14", "게시글 내용 14"),
                        Tuple.tuple("게시글 제목 15", "게시글 내용 15"),
                        Tuple.tuple("게시글 제목 16", "게시글 내용 16"),
                        Tuple.tuple("게시글 제목 17", "게시글 내용 17"),
                        Tuple.tuple("게시글 제목 18", "게시글 내용 18"),
                        Tuple.tuple("게시글 제목 19", "게시글 내용 19"),
                        Tuple.tuple("게시글 제목 20", "게시글 내용 20")
                );
    }

    @Test
    @DisplayName("게시글 리스트 조회 시, 페이지 번호가 1 미만이어서 예외가 발생한다.")
    void getArticleListPageableExceptionPageNumber() {
        // given
        PageServiceRequest request = PageServiceRequest.builder()
                .pageNumber(0)
                .build();

        // when, then
        assertThatThrownBy(() -> articleService.getArticleList(request))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("게시글 리스트 조회 시, 정렬 기준 값이 존재하지 않는 컬럼이라 예외가 발생한다.")
    void getArticleListPageableExceptionProperty() {
        // given
        PageServiceRequest request = PageServiceRequest.builder()
                .pageNumber(1)
                .property("idd")
                .build();

        // when, then
        assertThatThrownBy(() -> articleService.getArticleList(request))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("게시글 리스트 조회 시, 정렬 순서 값이 asc 또는 desc가 아니어서 예외가 발생한다.")
    void getArticleListPageableExceptionDirection() {
        // given
        PageServiceRequest request = PageServiceRequest.builder()
                .pageNumber(1)
                .direction("descc")
                .build();

        // when, then
        assertThatThrownBy(() -> articleService.getArticleList(request))
                .isInstanceOf(BusinessException.class);
    }

    private static Article toEntity(String title, String content, boolean deleted) {
        return Article.builder()
                .title(title)
                .content(content)
                .deleted(deleted)
                .build();
    }

    private static Article toEntity(String title, String content) {
        return Article.builder()
                .title(title)
                .content(content)
                .deleted(false)
                .build();
    }

}