package com.board.service.article;

import com.board.IntegrationTestSupport;
import com.board.service.article.request.ArticleCreateServiceRequest;
import com.board.service.article.response.ArticleResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class ArticleServiceTest extends IntegrationTestSupport {

    @Autowired
    private ArticleService articleService;

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
                .contains("안녕하세요. 50자를 맞춰보겠습니다. 아직 부족한가요? 제목은 50자를 초과하며 안됩니다.", "반갑습니다.");
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

}