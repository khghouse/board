package com.board.api.article;

import com.board.api.ControllerTestSupport;
import com.board.api.article.request.ArticleCreateRequest;
import com.board.service.article.ArticleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ArticleControllerTest extends ControllerTestSupport {

    @MockBean
    private ArticleService articleService;

    @Test
    @DisplayName("게시글을 등록하고 정상 응답한다.")
    void postArticle() throws Exception {
        //given
        ArticleCreateRequest request = ArticleCreateRequest.builder()
                .title("게시글 제목입니다.")
                .content("게시글 내용입니다.")
                .build();

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시글 등록할 때 제목은 필수입니다.")
    void postArticleWithoutTitle() throws Exception {
        // given
        ArticleCreateRequest request = ArticleCreateRequest.builder()
                .content("게시글 내용입니다.")
                .build();

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("게시글 제목을 입력해 주세요."));
    }

    @Test
    @DisplayName("게시글 등록할 때 제목이 공백이면 클라이언트 에러를 응답한다.")
    void postArticleBlankTitle() throws Exception {
        // given
        ArticleCreateRequest request = ArticleCreateRequest.builder()
                .title("     ")
                .content("게시글 내용입니다.")
                .build();

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("게시글 제목을 입력해 주세요."));
    }

    @Test
    @DisplayName("게시글 등록할 때 내용은 필수입니다.")
    void postArticleWithoutContent() throws Exception {
        // given
        ArticleCreateRequest request = ArticleCreateRequest.builder()
                .title("게시글 제목입니다.")
                .build();

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("게시글 내용을 입력해 주세요."));
    }

    @Test
    @DisplayName("게시글 등록할 때 내용이 공백이면 클라이언트 에러를 응답한다.")
    void postArticleBlankContent() throws Exception {
        // given
        ArticleCreateRequest request = ArticleCreateRequest.builder()
                .title("게시글 제목입니다.")
                .content("     ")
                .build();

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("게시글 내용을 입력해 주세요."));
    }

}