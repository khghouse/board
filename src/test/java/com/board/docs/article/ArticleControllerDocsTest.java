package com.board.docs.article;

import com.board.api.article.ArticleController;
import com.board.api.article.request.ArticleCreateRequest;
import com.board.api.article.request.ArticleRequest;
import com.board.api.article.request.ArticleUpdateRequest;
import com.board.docs.RestDocsSupport;
import com.board.service.article.ArticleService;
import com.board.service.article.response.ArticleResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ArticleControllerDocsTest extends RestDocsSupport {

    private final ArticleService articleService = mock(ArticleService.class);

    @Override
    protected Object initController() {
        return new ArticleController(articleService);
    }

    @Test
    @DisplayName("게시글 등록 API")
    void postArticle() throws Exception {
        // given
        ArticleCreateRequest request = ArticleCreateRequest.builder()
                .title("게시글 제목입니다.")
                .content("게시글 내용입니다.")
                .build();

        BDDMockito.given(articleService.postArticle(any()))
                .willReturn(ArticleResponse.builder()
                        .id(1L)
                        .title("게시글 제목입니다.")
                        .content("게시글 내용입니다.")
                        .build());

        // when, then
        mockMvc.perform(post("/api/v1/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document.document(
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING)
                                        .description("게시글 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING)
                                        .description("게시글 내용")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("HTTP 상태 코드"),
                                fieldWithPath("error").type(JsonFieldType.OBJECT)
                                        .description("에러 정보")
                                        .optional(),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("게시글 ID"),
                                fieldWithPath("data.title").type(JsonFieldType.STRING)
                                        .description("게시글 제목"),
                                fieldWithPath("data.content").type(JsonFieldType.STRING)
                                        .description("게시글 내용")
                        )
                ));
    }

    @Test
    @DisplayName("게시글 1건 조회 API")
    void getArticle() throws Exception {
        // given
        ArticleRequest request = ArticleRequest.builder()
                .id(1L)
                .build();

        BDDMockito.given(articleService.getArticle(anyLong()))
                .willReturn(ArticleResponse.builder()
                        .id(1L)
                        .title("게시글 제목입니다.")
                        .content("게시글 내용입니다.")
                        .build());

        // when, then
        mockMvc.perform(get("/api/v1/articles/{id}", request.getId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("id").description("게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("HTTP 상태 코드"),
                                fieldWithPath("error").type(JsonFieldType.OBJECT)
                                        .description("에러 정보")
                                        .optional(),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("게시글 ID"),
                                fieldWithPath("data.title").type(JsonFieldType.STRING)
                                        .description("게시글 제목"),
                                fieldWithPath("data.content").type(JsonFieldType.STRING)
                                        .description("게시글 내용")
                        )
                ));
    }

    @Test
    @DisplayName("게시글 수정 API")
    void putArticle() throws Exception {
        // given
        ArticleUpdateRequest request = ArticleUpdateRequest.builder()
                .title("게시글 제목")
                .content("게시글 내용.")
                .build();

        BDDMockito.given(articleService.putArticle(any()))
                .willReturn(ArticleResponse.builder()
                        .id(1L)
                        .title("게시글 제목")
                        .content("게시글 내용")
                        .build());

        // when, then
        mockMvc.perform(put("/api/v1/articles/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("id").description("게시글 ID")
                        ),
                        requestFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER)
                                        .description("게시글 ID")
                                        .ignored(),
                                fieldWithPath("title").type(JsonFieldType.STRING)
                                        .description("게시글 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING)
                                        .description("게시글 내용")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("HTTP 상태 코드"),
                                fieldWithPath("error").type(JsonFieldType.OBJECT)
                                        .description("에러 정보")
                                        .optional(),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("게시글 ID"),
                                fieldWithPath("data.title").type(JsonFieldType.STRING)
                                        .description("게시글 제목"),
                                fieldWithPath("data.content").type(JsonFieldType.STRING)
                                        .description("게시글 내용")
                        )
                ));
    }

    @Test
    @DisplayName("게시글 삭제 API")
    void deleteArticle() throws Exception {
        // given
        ArticleRequest request = ArticleRequest.builder()
                .id(1L)
                .build();

        // when, then
        mockMvc.perform(delete("/api/v1/articles/{id}", request.getId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("id").description("게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("HTTP 상태 코드"),
                                fieldWithPath("error").type(JsonFieldType.OBJECT)
                                        .description("에러 정보")
                                        .optional(),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터")
                                        .optional()
                        )
                ));
    }

}
