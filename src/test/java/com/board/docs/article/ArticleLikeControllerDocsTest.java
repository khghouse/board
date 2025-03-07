package com.board.docs.article;

import com.board.api.article.ArticleLikeController;
import com.board.service.article.ArticleLikeService;
import com.board.support.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class ArticleLikeControllerDocsTest extends RestDocsSupport {

    public static final String PATH = "/articles/{id}/likes";

    private final ArticleLikeService articleLikeService = mock(ArticleLikeService.class);

    @Override
    protected Object initController() {
        return new ArticleLikeController(articleLikeService);
    }

    @Test
    @DisplayName("게시글 좋아요 추가 API")
    void like() throws Exception {
        // when, then
        mockMvc.perform(post(PATH, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
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

    @Test
    @DisplayName("게시글 좋아요 취소 API")
    void unlike() throws Exception {
        // when, then
        mockMvc.perform(delete(PATH, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
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
