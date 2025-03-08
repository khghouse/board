package com.board.service.article;

import com.board.domain.article.Article;
import com.board.domain.article.ArticleRepository;
import com.board.domain.member.Member;
import com.board.domain.member.MemberRepository;
import com.board.dto.page.PageResponse;
import com.board.dto.page.PageServiceRequest;
import com.board.exception.BusinessException;
import com.board.service.article.request.ArticleServiceRequest;
import com.board.service.article.response.ArticleDetailResponse;
import com.board.service.article.response.ArticleResponse;
import com.board.support.IntegrationTestSupport;
import jakarta.persistence.EntityManager;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.board.enumeration.ErrorCode.*;
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

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        entityManager.createNativeQuery("ALTER TABLE member ALTER COLUMN id RESTART WITH 1").executeUpdate();
    }

    @Test
    @DisplayName("게시글을 등록하고 검증한다.")
    void createArticle() {
        // given
        Member member = createMember();

        ArticleServiceRequest request = ArticleServiceRequest.withTitleAndContent("안녕하세요.", "반갑습니다.");

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
        Member member = createMember();
        Article article = createArticle(false, member);

        // when
        ArticleDetailResponse result = articleService.getArticle(article.getId());

        // then
        assertThat(result.getId()).isEqualTo(article.getId());
        assertThat(result.getTitle()).isEqualTo("안녕하세요.");
        assertThat(result.getContent()).isEqualTo("반갑습니다.");
        assertThat(result.getMember().getEmail()).isEqualTo("khghouse@daum.net");
    }

    @Test
    @DisplayName("게시글 1건의 조회 결과가 없어서 예외가 발생한다.")
    void getArticleNotFound() {
        // when, then
        assertThatThrownBy(() -> articleService.getArticle(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ARTICLE_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("게시글을 수정하고 검증한다.")
    void updateArticle() {
        // given
        Member member = createMember();

        Article article = Article.builder()
                .title("안녕하세요.")
                .content("반갑습니다.")
                .member(member)
                .deleted(false)
                .build();
        articleRepository.save(article);
        entityManager.flush();
        entityManager.clear();

        ArticleServiceRequest request = ArticleServiceRequest.of(article.getId(), "안녕하세요!", "반갑습니다!");

        // when
        articleService.updateArticle(request, member.getId());
        entityManager.flush();
        entityManager.clear();

        // then
        Article result = articleRepository.findById(article.getId()).orElseThrow();

        assertThat(result).extracting("title", "content")
                .contains("안녕하세요!", "반갑습니다!");
    }

    @Test
    @DisplayName("수정하려는 게시글 정보가 없어서 예외가 발생한다.")
    void updateArticleNotFound() {
        // given
        ArticleServiceRequest request = ArticleServiceRequest.of(1L, "수정할 게시글", "없음");

        // when, then
        assertThatThrownBy(() -> articleService.updateArticle(request, 1L))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ARTICLE_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("본인이 작성하지 않은 게시글을 수정하려고 한다면 예외가 발생한다.")
    void updateArticleInvalidWriter() {
        // given
        Member member = createMember();
        Article article = createArticle(false, member);

        ArticleServiceRequest request = ArticleServiceRequest.of(article.getId(), "안녕하세요!", "반갑습니다!");

        // when, then
        assertThatThrownBy(() -> articleService.updateArticle(request, 2L))
                .isInstanceOf(BusinessException.class)
                .hasMessage(INVALID_WRITER.getMessage());
    }

    @Test
    @DisplayName("등록된 게시글을 삭제하고 검증한다.")
    void deleteArticle() {
        // given
        Member member = createMember();
        Article article = createArticle(false, member);

        // when
        articleService.deleteArticle(article.getId(), member.getId());

        // then
        Article result = articleRepository.findById(article.getId()).orElseThrow();
        assertThat(result.getDeleted()).isTrue();
    }

    @Test
    @DisplayName("등록된 게시글을 삭제하려고 했는데 이미 삭제된 게시글이라 예외가 발생한다.")
    void deleteArticleAlreadyDeleted() {
        // given
        Member member = createMember();
        Article article = createArticle(true, member);

        // when, then
        assertThatThrownBy(() -> articleService.deleteArticle(article.getId(), member.getId()))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ALREADY_DELETED.getMessage());
    }

    @Test
    @DisplayName("삭제하려는 게시글 정보가 없어서 예외가 발생한다.")
    void deleteArticleNotFound() {
        // when, then
        assertThatThrownBy(() -> articleService.deleteArticle(1L, 1L))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ARTICLE_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("본인이 작성하지 않은 게시글을 삭제하려고 한다면 예외가 발생한다.")
    void deleteArticleInvalidWriter() {
        // given
        Member member = createMember();
        Article article = createArticle(false, member);

        // when, then
        assertThatThrownBy(() -> articleService.deleteArticle(article.getId(), 2L))
                .isInstanceOf(BusinessException.class)
                .hasMessage(INVALID_WRITER.getMessage());
    }

    @Test
    @DisplayName("게시글 리스트를 조회하고 검증한다.")
    void getArticleList() {
        // given
        List<Member> members = IntStream.range(1, 5)
                .mapToObj(i -> toEntity("khghouse" + i + "@daum.net"))
                .collect(Collectors.toList());
        memberRepository.saveAll(members);

        Article article1 = toEntity("게시글 제목 1", "게시글 내용 1", false, members.get(0));
        Article article2 = toEntity("게시글 제목 2", "게시글 내용 2", true, members.get(1));
        Article article3 = toEntity("게시글 제목 3", "게시글 내용 3", false, members.get(2));
        Article article4 = toEntity("게시글 제목 4", "게시글 내용 4", false, members.get(3));
        articleRepository.saveAll(List.of(article1, article2, article3, article4));

        PageServiceRequest request = PageServiceRequest.withDefault();

        entityManager.flush();
        entityManager.clear();

        // when
        PageResponse result = articleService.getArticleList(request);

        // then
        assertThat(result.getPageInfomation().getPageNumber()).isEqualTo(1);
        assertThat(result.getPageInfomation().getTotalPages()).isEqualTo(1);
        assertThat(result.getPageInfomation().getTotalElements()).isEqualTo(3);
        assertThat(result.getPageInfomation().getIsLast()).isTrue();
        assertThat(result.getContents()).hasSize(3)
                .extracting("title", "content", "member.email")
                .containsExactly(
                        Tuple.tuple("게시글 제목 4", "게시글 내용 4", "khghouse4@daum.net"),
                        Tuple.tuple("게시글 제목 3", "게시글 내용 3", "khghouse3@daum.net"),
                        Tuple.tuple("게시글 제목 1", "게시글 내용 1", "khghouse1@daum.net")
                );
    }

    @Test
    @DisplayName("게시글 리스트 사이즈가 0이면 빈 배열을 응답한다.")
    void getArticleListSizeZero() {
        // given
        PageServiceRequest request = PageServiceRequest.withDefault();

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
        List<Member> members = IntStream.range(1, 21)
                .mapToObj(i -> toEntity("khghouse" + i + "@daum.net"))
                .collect(Collectors.toList());
        memberRepository.saveAll(members);

        List<Article> articles = IntStream.range(1, 21)
                .mapToObj(i -> toEntity("게시글 제목 " + i, "게시글 내용 " + i, false, members.get(i - 1)))
                .collect(Collectors.toList());
        articleRepository.saveAll(articles);

        PageServiceRequest request = PageServiceRequest.of(1, 5, "desc", "id");

        // when
        PageResponse result = articleService.getArticleList(request);

        // then
        assertThat(result.getPageInfomation().getPageNumber()).isEqualTo(1);
        assertThat(result.getPageInfomation().getTotalPages()).isEqualTo(4);
        assertThat(result.getPageInfomation().getTotalElements()).isEqualTo(20);
        assertThat(result.getPageInfomation().getIsLast()).isFalse();
        assertThat(result.getContents()).hasSize(5)
                .extracting("title", "content", "member.email")
                .containsExactly(
                        Tuple.tuple("게시글 제목 20", "게시글 내용 20", "khghouse20@daum.net"),
                        Tuple.tuple("게시글 제목 19", "게시글 내용 19", "khghouse19@daum.net"),
                        Tuple.tuple("게시글 제목 18", "게시글 내용 18", "khghouse18@daum.net"),
                        Tuple.tuple("게시글 제목 17", "게시글 내용 17", "khghouse17@daum.net"),
                        Tuple.tuple("게시글 제목 16", "게시글 내용 16", "khghouse16@daum.net")
                );
    }

    @Test
    @DisplayName("페이징 처리된 게시글 리스트의 마지막 페이지 조회하고 검증한다.")
    void getArticleListPageableLastPage() {
        // given
        List<Member> members = IntStream.range(1, 21)
                .mapToObj(i -> toEntity("khghouse" + i + "@daum.net"))
                .collect(Collectors.toList());
        memberRepository.saveAll(members);

        List<Article> articles = IntStream.range(1, 21)
                .mapToObj(i -> toEntity("게시글 제목 " + i, "게시글 내용 " + i, false, members.get(i - 1)))
                .collect(Collectors.toList());
        articleRepository.saveAll(articles);

        PageServiceRequest request = PageServiceRequest.of(5, 4, "desc", "id");

        // when
        PageResponse result = articleService.getArticleList(request);

        // then
        assertThat(result.getPageInfomation().getPageNumber()).isEqualTo(5);
        assertThat(result.getPageInfomation().getTotalPages()).isEqualTo(5);
        assertThat(result.getPageInfomation().getTotalElements()).isEqualTo(20);
        assertThat(result.getPageInfomation().getIsLast()).isTrue();
        assertThat(result.getContents()).hasSize(4)
                .extracting("title", "content", "member.email")
                .containsExactly(
                        Tuple.tuple("게시글 제목 4", "게시글 내용 4", "khghouse4@daum.net"),
                        Tuple.tuple("게시글 제목 3", "게시글 내용 3", "khghouse3@daum.net"),
                        Tuple.tuple("게시글 제목 2", "게시글 내용 2", "khghouse2@daum.net"),
                        Tuple.tuple("게시글 제목 1", "게시글 내용 1", "khghouse1@daum.net")
                );
    }

    @Test
    @DisplayName("ID 오름차순 정렬된 게시글 리스트의 조회하고 검증한다.")
    void getArticleListPageablOrderByIdAsc() {
        // given
        List<Member> members = IntStream.range(1, 21)
                .mapToObj(i -> toEntity("khghouse" + i + "@daum.net"))
                .collect(Collectors.toList());
        memberRepository.saveAll(members);

        List<Article> articles = IntStream.range(1, 21)
                .mapToObj(i -> toEntity("게시글 제목 " + i, "게시글 내용 " + i, false, members.get(i - 1)))
                .collect(Collectors.toList());
        articleRepository.saveAll(articles);

        PageServiceRequest request = PageServiceRequest.withPageAndSortDirection(2, 10, "asc");

        // when
        PageResponse result = articleService.getArticleList(request);

        // then
        assertThat(result.getPageInfomation().getPageNumber()).isEqualTo(2);
        assertThat(result.getPageInfomation().getTotalPages()).isEqualTo(2);
        assertThat(result.getPageInfomation().getTotalElements()).isEqualTo(20);
        assertThat(result.getPageInfomation().getIsLast()).isTrue();
        assertThat(result.getContents()).hasSize(10)
                .extracting("title", "content", "member.email")
                .containsExactly(
                        Tuple.tuple("게시글 제목 11", "게시글 내용 11", "khghouse11@daum.net"),
                        Tuple.tuple("게시글 제목 12", "게시글 내용 12", "khghouse12@daum.net"),
                        Tuple.tuple("게시글 제목 13", "게시글 내용 13", "khghouse13@daum.net"),
                        Tuple.tuple("게시글 제목 14", "게시글 내용 14", "khghouse14@daum.net"),
                        Tuple.tuple("게시글 제목 15", "게시글 내용 15", "khghouse15@daum.net"),
                        Tuple.tuple("게시글 제목 16", "게시글 내용 16", "khghouse16@daum.net"),
                        Tuple.tuple("게시글 제목 17", "게시글 내용 17", "khghouse17@daum.net"),
                        Tuple.tuple("게시글 제목 18", "게시글 내용 18", "khghouse18@daum.net"),
                        Tuple.tuple("게시글 제목 19", "게시글 내용 19", "khghouse19@daum.net"),
                        Tuple.tuple("게시글 제목 20", "게시글 내용 20", "khghouse20@daum.net")
                );
    }

    @Test
    @DisplayName("게시글 리스트 조회 시, 페이지 번호가 1 미만이어서 예외가 발생한다.")
    void getArticleListPageableExceptionPageNumber() {
        // given
        PageServiceRequest request = PageServiceRequest.withPageNumber(0);

        // when, then
        assertThatThrownBy(() -> articleService.getArticleList(request))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("게시글 리스트 조회 시, 정렬 기준 값이 존재하지 않는 컬럼이라 예외가 발생한다.")
    void getArticleListPageableExceptionProperty() {
        // given
        PageServiceRequest request = PageServiceRequest.withPageNumberAndSortByColumn(1, "idd");

        // when, then
        assertThatThrownBy(() -> articleService.getArticleList(request))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("게시글 리스트 조회 시, 정렬 순서 값이 asc 또는 desc가 아니어서 예외가 발생한다.")
    void getArticleListPageableExceptionDirection() {
        // given
        PageServiceRequest request = PageServiceRequest.withPageNumberAndSortDirection(1, "descc");

        // when, then
        assertThatThrownBy(() -> articleService.getArticleList(request))
                .isInstanceOf(BusinessException.class);
    }

    private Member createMember() {
        Member member = Member.builder()
                .email("khghouse@daum.net")
                .password("Password12#$")
                .build();

        memberRepository.save(member);
        return member;
    }

    private Article createArticle(boolean deleted, Member member) {
        Article article = Article.builder()
                .title("안녕하세요.")
                .content("반갑습니다.")
                .deleted(deleted)
                .member(member)
                .build();

        articleRepository.save(article);
        return article;
    }

    private Article toEntity(String title, String content, boolean deleted, Member member) {
        return Article.builder()
                .title(title)
                .content(content)
                .deleted(deleted)
                .member(member)
                .build();
    }

    private Member toEntity(String email) {
        return Member.builder()
                .email(email)
                .password("Password12#$")
                .build();
    }

}