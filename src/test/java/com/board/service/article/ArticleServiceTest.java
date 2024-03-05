package com.board.service.article;

import com.board.IntegrationTestSupport;
import com.board.api.article.request.ArticleRequest;
import com.board.domain.article.Article;
import com.board.domain.article.ArticleRepository;
import com.board.service.article.request.ArticleCreateServiceRequest;
import com.board.service.article.request.ArticleUpdateServiceRequest;
import com.board.service.article.response.ArticleResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
        Article article = Article.builder()
                .title("게시글 제목")
                .content("게시글 내용")
                .build();

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
        Article article = Article.builder()
                .title("게시글 제목")
                .content("게시글 내용")
                .build();

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
        Article article = Article.builder()
                .title("게시글 제목")
                .content("게시글 내용")
                .build();

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
        Article article = Article.builder()
                .title("게시글 제목")
                .content("게시글 내용")
                .build();

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
        Article article = Article.builder()
                .title("게시글 제목")
                .content("게시글 내용")
                .build();

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
        Article article = Article.builder()
                .title("게시글 제목")
                .content("게시글 내용")
                .deleted(false)
                .build();

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
        Article article = Article.builder()
                .title("게시글 제목")
                .content("게시글 내용")
                .deleted(true)
                .build();

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

}