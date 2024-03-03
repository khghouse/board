package com.board.api.article;

import com.board.api.ControllerTestSupport;
import com.board.api.article.request.ArticleCreateRequest;
import com.board.api.article.request.ArticleRequest;
import com.board.api.article.request.ArticleUpdateRequest;
import com.board.service.article.ArticleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ArticleControllerTest extends ControllerTestSupport {

    @MockBean
    private ArticleService articleService;

    @Test
    @DisplayName("게시글을 등록하고 정상 응답한다.")
    void postArticle() throws Exception {
        // given
        ArticleCreateRequest request = ArticleCreateRequest.builder()
                .title("게시글 제목입니다.")
                .content("게시글 내용입니다.")
                .build();

        // when, then
        mockMvc.perform(post("/api/v1/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시글을 등록할 때 제목은 필수입니다.")
    void postArticleWithoutTitle() throws Exception {
        // given
        ArticleCreateRequest request = ArticleCreateRequest.builder()
                .content("게시글 내용입니다.")
                .build();

        // when, then
        mockMvc.perform(post("/api/v1/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("게시글 제목을 입력해 주세요."));
    }

    @Test
    @DisplayName("게시글을 등록할 때 제목이 공백이면 클라이언트 에러를 응답한다.")
    void postArticleBlankTitle() throws Exception {
        // given
        ArticleCreateRequest request = ArticleCreateRequest.builder()
                .title("     ")
                .content("게시글 내용입니다.")
                .build();

        // when, then
        mockMvc.perform(post("/api/v1/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("게시글 제목을 입력해 주세요."));
    }

    @Test
    @DisplayName("게시글을 등록할 때 내용은 필수입니다.")
    void postArticleWithoutContent() throws Exception {
        // given
        ArticleCreateRequest request = ArticleCreateRequest.builder()
                .title("게시글 제목입니다.")
                .build();

        // when, then
        mockMvc.perform(post("/api/v1/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("게시글 내용을 입력해 주세요."));
    }

    @Test
    @DisplayName("게시글을 등록할 때 내용이 공백이면 클라이언트 에러를 응답한다.")
    void postArticleBlankContent() throws Exception {
        // given
        ArticleCreateRequest request = ArticleCreateRequest.builder()
                .title("게시글 제목입니다.")
                .content("     ")
                .build();

        // when, then
        mockMvc.perform(post("/api/v1/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("게시글 내용을 입력해 주세요."));
    }

    @Test
    @DisplayName("게시글 1건을 조회하고 정상 응답한다.")
    void getArticle() throws Exception {
        // given
        ArticleRequest request = ArticleRequest.builder()
                .id(1L)
                .build();

        // when, then
        mockMvc.perform(get("/api/v1/articles/{id}", request.getId()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시글 1건을 조회할 때 ID값이 0이면 클라이언트 에러를 응답한다.")
    void getArticleIdZero() throws Exception {
        // given
        ArticleRequest request = ArticleRequest.builder()
                .id(0L)
                .build();

        // when, then
        mockMvc.perform(get("/api/v1/articles/{id}", request.getId()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("게시글 ID는 양수여야 합니다."));
    }

    @Test
    @DisplayName("게시글 1건을 조회할 때 ID값이 음수이면 클라이언트 에러를 응답한다.")
    void getArticleIdNegative() throws Exception {
        // given
        ArticleRequest request = ArticleRequest.builder()
                .id(-1L)
                .build();

        // when, then
        mockMvc.perform(get("/api/v1/articles/{id}", request.getId()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("게시글 ID는 양수여야 합니다."));
    }

    @Test
    @DisplayName("게시글을 수정하고 정상 응답한다.")
    void putArticle() throws Exception {
        // given
        ArticleUpdateRequest request = ArticleUpdateRequest.builder()
                .title("게시글 제목입니다.")
                .content("게시글 내용입니다.")
                .build();

        // when, then
        mockMvc.perform(put("/api/v1/articles/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시글을 수정할 때 제목은 필수입니다.")
    void putArticleWithoutTitle() throws Exception {
        // given
        ArticleUpdateRequest request = ArticleUpdateRequest.builder()
                .content("게시글 내용입니다.")
                .build();

        // when, then
        mockMvc.perform(put("/api/v1/articles/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("게시글 제목을 입력해 주세요."));
    }

    @Test
    @DisplayName("게시글을 수정할 때 제목이 공백이면 클라이언트 에러를 응답한다.")
    void putArticleBlankTitle() throws Exception {
        // given
        ArticleUpdateRequest request = ArticleUpdateRequest.builder()
                .title("     ")
                .content("게시글 내용입니다.")
                .build();

        // when, then
        mockMvc.perform(put("/api/v1/articles/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("게시글 제목을 입력해 주세요."));
    }

    @Test
    @DisplayName("게시글을 수정할 때 내용은 필수입니다.")
    void putArticleWithoutContent() throws Exception {
        // given
        ArticleUpdateRequest request = ArticleUpdateRequest.builder()
                .title("게시글 제목입니다.")
                .build();

        // when, then
        mockMvc.perform(put("/api/v1/articles/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("게시글 내용을 입력해 주세요."));
    }

    @Test
    @DisplayName("게시글을 수정할 때 내용이 공백이면 클라이언트 에러를 응답한다.")
    void putArticleBlankContent() throws Exception {
        // given
        ArticleUpdateRequest request = ArticleUpdateRequest.builder()
                .title("게시글 제목입니다.")
                .content("     ")
                .build();

        // when, then
        mockMvc.perform(put("/api/v1/articles/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("게시글 내용을 입력해 주세요."));
    }

    @Test
    @DisplayName("게시글을 수정할 때 ID값이 0이면 클라이언트 에러를 응답한다.")
    void putArticleIdZero() throws Exception {
        // given
        ArticleUpdateRequest request = ArticleUpdateRequest.builder()
                .id(0L)
                .title("게시글 제목입니다.")
                .content("게시글 내용입니다.")
                .build();

        // when, then
        mockMvc.perform(put("/api/v1/articles/{id}", request.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("게시글 ID는 양수여야 합니다."));
    }

    @Test
    @DisplayName("게시글을 수정할 때 ID값이 음수이면 클라이언트 에러를 응답한다.")
    void putArticleIdNegative() throws Exception {
        // given
        ArticleUpdateRequest request = ArticleUpdateRequest.builder()
                .id(-1L)
                .title("게시글 제목입니다.")
                .content("게시글 내용입니다.")
                .build();

        // when, then
        mockMvc.perform(put("/api/v1/articles/{id}", request.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("게시글 ID는 양수여야 합니다."));
    }

}