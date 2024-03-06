package com.board.service.article;

import com.board.IntegrationTestSupport;
import com.board.api.article.request.ArticleRequest;
import com.board.domain.article.Article;
import com.board.domain.article.ArticleRepository;
import com.board.service.article.request.ArticleCreateServiceRequest;
import com.board.service.article.request.ArticleUpdateServiceRequest;
import com.board.service.article.response.ArticleResponse;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class ArticleServiceTest extends IntegrationTestSupport {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    @DisplayName("게시글을 등록하고 검증한다.")
    void postArticle() {
        // given
        ArticleCreateServiceRequest request = ArticleCreateServiceRequest.builder()
                .title("안녕하세요.")
                .content("반갑습니다.")
                .build();

        // when
        ArticleResponse result = articleService.postArticle(request);

        // then
        assertThat(result).extracting("title", "content")
                .contains("안녕하세요.", "반갑습니다.");
    }

    @Test
    @DisplayName("게시글 제목 글자수가 50자로 게시글은 정상 등록된다.")
    void postArticleBoundaryValue() {
        // given
        ArticleCreateServiceRequest request = ArticleCreateServiceRequest.builder()
                .title("안녕하세요. 50자에 맞춰보겠습니다. 아직 부족한가요? 제목은 50자를 초과하며 안됩니다.")
                .content("반갑습니다.")
                .build();

        // when
        ArticleResponse result = articleService.postArticle(request);

        // then
        assertThat(request.getTitle().length()).isEqualTo(50);
        assertThat(result).extracting("title", "content")
                .contains("안녕하세요. 50자에 맞춰보겠습니다. 아직 부족한가요? 제목은 50자를 초과하며 안됩니다.", "반갑습니다.");
    }

    @Test
    @DisplayName("게시글 제목을 50자 초과하여 예외가 발생한다.")
    void postArticleExceedingTitle() {
        // given
        ArticleCreateServiceRequest request = ArticleCreateServiceRequest.builder()
                .title("안녕하세요. 50자를 한번 넘겨보겠습니다. 아직 부족한가요?? 제목은 50자를 초과하며 안됩니다.")
                .content("반갑습니다.")
                .build();

        // when, then
        assertThatThrownBy(() -> articleService.postArticle(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("게시글 제목은 50자를 초과할 수 없습니다.");
    }

    @Test
    @DisplayName("게시글 내용을 500자 초과하여 예외가 발생한다.")
    void postArticleExceedingContent() {
        // given
        ArticleCreateServiceRequest request = ArticleCreateServiceRequest.builder()
                .title("안녕하세요.")
                .content("반갑습니다. 설마 내용을 500자 초과하려는 건가요? 이건 복붙을 참을 수 없습니다. 화이팅!! 화이팅!!!" +
                        "설마 내용을 500자 초과하려는 건가요? 이건 복붙을 참을 수 없습니다. 화이팅!! 화이팅!!!" +
                        "설마 내용을 500자 초과하려는 건가요? 이건 복붙을 참을 수 없습니다. 화이팅!! 화이팅!!!" +
                        "설마 내용을 500자 초과하려는 건가요? 이건 복붙을 참을 수 없습니다. 화이팅!! 화이팅!!!" +
                        "설마 내용을 500자 초과하려는 건가요? 이건 복붙을 참을 수 없습니다. 화이팅!! 화이팅!!!" +
                        "설마 내용을 500자 초과하려는 건가요? 이건 복붙을 참을 수 없습니다. 화이팅!! 화이팅!!!" +
                        "설마 내용을 500자 초과하려는 건가요? 이건 복붙을 참을 수 없습니다. 화이팅!! 화이팅!!!" +
                        "설마 내용을 500자 초과하려는 건가요? 이건 복붙을 참을 수 없습니다. 화이팅!! 화이팅!!!" +
                        "설마 내용을 500자 초과하려는 건가요? 이건 복붙을 참을 수 없습니다. 화이팅!! 화이팅!!!" +
                        "설마 내용을 500자 초과하려는 건가요? 이건 복붙을 참을 수 없습니다. 화이팅!! 화이팅!!!")
                .build();

        // when, then
        assertThatThrownBy(() -> articleService.postArticle(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("게시글 내용은 500자를 초과할 수 없습니다.");
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
    void putArticle() {
        // given
        Article article = toEntity("게시글 제목", "게시글 내용");
        articleRepository.save(article);

        ArticleUpdateServiceRequest request = ArticleUpdateServiceRequest.builder()
                .id(article.getId())
                .title("안녕하세요.")
                .content("반갑습니다.")
                .build();

        // when
        ArticleResponse result = articleService.putArticle(request);

        // then
        assertThat(result).extracting("title", "content")
                .contains("안녕하세요.", "반갑습니다.");
    }

    @Test
    @DisplayName("수정하려는 게시글 정보가 없어서 예외가 발생한다.")
    void putArticleNotFind() {
        // given
        ArticleUpdateServiceRequest request = ArticleUpdateServiceRequest.builder()
                .id(1L)
                .build();

        // when, then
        assertThatThrownBy(() -> articleService.putArticle(request))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("게시글 정보가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("게시글 제목 글자수가 50자로 게시글은 정상 수정된다.")
    void putArticleBoundaryValue() {
        // given
        Article article = toEntity("게시글 제목", "게시글 내용");
        articleRepository.save(article);

        ArticleUpdateServiceRequest request = ArticleUpdateServiceRequest.builder()
                .id(article.getId())
                .title("안녕하세요. 50자에 맞춰보겠습니다. 아직 부족한가요? 제목은 50자를 초과하며 안됩니다.")
                .content("반갑습니다.")
                .build();

        // when
        ArticleResponse result = articleService.putArticle(request);

        // then
        assertThat(request.getTitle().length()).isEqualTo(50);
        assertThat(result).extracting("title", "content")
                .contains("안녕하세요. 50자에 맞춰보겠습니다. 아직 부족한가요? 제목은 50자를 초과하며 안됩니다.", "반갑습니다.");
    }

    @Test
    @DisplayName("게시글 제목을 50자 초과하여 예외가 발생한다.")
    void putArticleExceedingTitle() {
        // given
        Article article = toEntity("게시글 제목", "게시글 내용");
        articleRepository.save(article);

        ArticleUpdateServiceRequest request = ArticleUpdateServiceRequest.builder()
                .id(article.getId())
                .title("안녕하세요. 50자를 한번 넘겨보겠습니다. 아직 부족한가요?? 제목은 50자를 초과하며 안됩니다.")
                .content("반갑습니다.")
                .build();

        // when, then
        assertThatThrownBy(() -> articleService.putArticle(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("게시글 제목은 50자를 초과할 수 없습니다.");
    }

    @Test
    @DisplayName("게시글 내용을 500자 초과하여 예외가 발생한다.")
    void putArticleExceedingContent() {
        // given
        Article article = toEntity("게시글 제목", "게시글 내용");
        articleRepository.save(article);

        ArticleUpdateServiceRequest request = ArticleUpdateServiceRequest.builder()
                .id(article.getId())
                .title("안녕하세요.")
                .content("반갑습니다. 설마 내용을 500자 초과하려는 건가요? 이건 복붙을 참을 수 없습니다. 화이팅!! 화이팅!!!" +
                        "설마 내용을 500자 초과하려는 건가요? 이건 복붙을 참을 수 없습니다. 화이팅!! 화이팅!!!" +
                        "설마 내용을 500자 초과하려는 건가요? 이건 복붙을 참을 수 없습니다. 화이팅!! 화이팅!!!" +
                        "설마 내용을 500자 초과하려는 건가요? 이건 복붙을 참을 수 없습니다. 화이팅!! 화이팅!!!" +
                        "설마 내용을 500자 초과하려는 건가요? 이건 복붙을 참을 수 없습니다. 화이팅!! 화이팅!!!" +
                        "설마 내용을 500자 초과하려는 건가요? 이건 복붙을 참을 수 없습니다. 화이팅!! 화이팅!!!" +
                        "설마 내용을 500자 초과하려는 건가요? 이건 복붙을 참을 수 없습니다. 화이팅!! 화이팅!!!" +
                        "설마 내용을 500자 초과하려는 건가요? 이건 복붙을 참을 수 없습니다. 화이팅!! 화이팅!!!" +
                        "설마 내용을 500자 초과하려는 건가요? 이건 복붙을 참을 수 없습니다. 화이팅!! 화이팅!!!" +
                        "설마 내용을 500자 초과하려는 건가요? 이건 복붙을 참을 수 없습니다. 화이팅!! 화이팅!!!")
                .build();

        // when, then
        assertThatThrownBy(() -> articleService.putArticle(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("게시글 내용은 500자를 초과할 수 없습니다.");
    }

    @Test
    @DisplayName("등록된 게시글을 삭제하고 검증한다.")
    void deleteArticle() {
        // given
        Article article = toEntity("게시글 제목", "게시글 내용", false);
        articleRepository.save(article);

        ArticleRequest request = ArticleRequest.builder()
                .id(article.getId())
                .build();

        // when
        articleService.deleteArticle(request.getId());

        // then
        Article result = articleRepository.findById(request.getId()).get();
        assertThat(result.getDeleted()).isTrue();
    }

    @Test
    @DisplayName("등록된 게시글을 삭제하려고 했는데 이미 삭제된 게시글이라 예외가 발생한다.")
    void deleteArticleAlreadyDeleted() {
        // given
        Article article = toEntity("게시글 제목", "게시글 내용", true);
        articleRepository.save(article);

        ArticleRequest request = ArticleRequest.builder()
                .id(article.getId())
                .build();

        // when, then
        assertThatThrownBy(() -> articleService.deleteArticle(request.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 삭제된 게시글입니다.");
    }

    @Test
    @DisplayName("삭제하려는 게시글 정보가 없어서 예외가 발생한다.")
    void deleteArticleNotFind() {
        // given
        ArticleRequest request = ArticleRequest.builder()
                .id(1L)
                .build();

        // when, then
        assertThatThrownBy(() -> articleService.deleteArticle(request.getId()))
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

        // when
        List<ArticleResponse> result = articleService.getArticleList();

        // then
        assertThat(result).hasSize(3)
                .extracting("title", "content")
                .containsExactly(
                        Tuple.tuple("게시글 제목 4", "게시글 내용 4"),
                        Tuple.tuple("게시글 제목 3", "게시글 내용 3"),
                        Tuple.tuple("게시글 제목 1", "게시글 내용 1")
                );
    }

    @Test
    @DisplayName("게시글 리스트 사이즈가 0이면 빈 배열을 응답한다.")
    void getArticleListSizeZero() {
        // when
        List<ArticleResponse> result = articleService.getArticleList();

        // then
        assertThat(result).isEmpty();
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