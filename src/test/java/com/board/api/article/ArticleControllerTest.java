package com.board.api.article;

import com.board.api.article.request.ArticleRequest;
import com.board.support.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ArticleControllerTest extends ControllerTestSupport {

    @Test
    @DisplayName("게시글을 등록하고 정상 응답한다.")
    void createArticle() throws Exception {
        // given
        ArticleRequest request = ArticleRequest.builder()
                .title("게시글 제목입니다.")
                .content("게시글 내용입니다.")
                .build();

        // when, then
        mockMvc.perform(post("/api/v1/articles")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시글을 등록할 때 제목은 필수입니다.")
    void createArticleWithoutTitle() throws Exception {
        // given
        ArticleRequest request = ArticleRequest.builder()
                .content("게시글 내용입니다.")
                .build();

        // when, then
        mockMvc.perform(post("/api/v1/articles")
                        .with(csrf())
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
    void createArticleBlankTitle() throws Exception {
        // given
        ArticleRequest request = ArticleRequest.builder()
                .title("     ")
                .content("게시글 내용입니다.")
                .build();

        // when, then
        mockMvc.perform(post("/api/v1/articles")
                        .with(csrf())
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
    void createArticleWithoutContent() throws Exception {
        // given
        ArticleRequest request = ArticleRequest.builder()
                .title("게시글 제목입니다.")
                .build();

        // when, then
        mockMvc.perform(post("/api/v1/articles")
                        .with(csrf())
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
    void createArticleBlankContent() throws Exception {
        // given
        ArticleRequest request = ArticleRequest.builder()
                .title("게시글 제목입니다.")
                .content("     ")
                .build();

        // when, then
        mockMvc.perform(post("/api/v1/articles")
                        .with(csrf())
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
        // when, then
        mockMvc.perform(get("/api/v1/articles/{id}", 1L)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시글 1건을 조회할 때 ID값이 숫자 타입이 아니면 클라이언트 에러를 응답한다.")
    void getArticleIdTypeMismatch() throws Exception {
        // when, then
        mockMvc.perform(get("/api/v1/articles/{id}", "null")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("요청 파라미터 타입이 올바르지 않습니다."));
    }

    @Test
    @DisplayName("게시글을 수정하고 정상 응답한다.")
    void updateArticle() throws Exception {
        // given
        ArticleRequest request = ArticleRequest.builder()
                .title("게시글 제목입니다.")
                .content("게시글 내용입니다.")
                .build();

        // when, then
        mockMvc.perform(put("/api/v1/articles/{id}", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시글을 수정할 때 제목은 필수입니다.")
    void updateArticleWithoutTitle() throws Exception {
        // given
        ArticleRequest request = ArticleRequest.builder()
                .content("게시글 내용입니다.")
                .build();

        // when, then
        mockMvc.perform(put("/api/v1/articles/{id}", 1L)
                        .with(csrf())
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
    void updateArticleBlankTitle() throws Exception {
        // given
        ArticleRequest request = ArticleRequest.builder()
                .title("     ")
                .content("게시글 내용입니다.")
                .build();

        // when, then
        mockMvc.perform(put("/api/v1/articles/{id}", 1L)
                        .with(csrf())
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
    void updateArticleWithoutContent() throws Exception {
        // given
        ArticleRequest request = ArticleRequest.builder()
                .title("게시글 제목입니다.")
                .build();

        // when, then
        mockMvc.perform(put("/api/v1/articles/{id}", 1L)
                        .with(csrf())
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
    void updateArticleBlankContent() throws Exception {
        // given
        ArticleRequest request = ArticleRequest.builder()
                .title("게시글 제목입니다.")
                .content("     ")
                .build();

        // when, then
        mockMvc.perform(put("/api/v1/articles/{id}", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("게시글 내용을 입력해 주세요."));
    }

    @Test
    @DisplayName("게시글을 수정할 때 ID값이 숫자 타입이 아니면 클라이언트 에러를 응답한다.")
    void updateArticleIdTypeMismatch() throws Exception {
        // given
        ArticleRequest request = ArticleRequest.builder()
                .title("게시글 제목입니다.")
                .content("게시글 내용입니다.")
                .build();

        // when, then
        mockMvc.perform(put("/api/v1/articles/{id}", "article")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("요청 파라미터 타입이 올바르지 않습니다."));
    }

    @Test
    @DisplayName("게시글을 삭제하고 정상 응답한다.")
    void deleteArticle() throws Exception {
        // when, then
        mockMvc.perform(delete("/api/v1/articles/{id}", 1L)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시글을 삭제할 때 ID값이 숫자 타입이 아니면 클라이언트 에러를 응답한다.")
    void deleteArticleIdTypeMismatch() throws Exception {
        // when, then
        mockMvc.perform(delete("/api/v1/articles/{id}", "id")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("요청 파라미터 타입이 올바르지 않습니다."));
    }

    @Test
    @DisplayName("게시글 리스트를 조회하고 정상 응답한다.")
    void getArticleList() throws Exception {
        // when, then
        mockMvc.perform(get("/api/v1/articles")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());
    }

}