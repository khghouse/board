package com.board.docs.article;

import com.board.domain.comment.controller.CommentController;
import com.board.domain.comment.dto.request.CommentRequest;
import com.board.domain.comment.service.CommentService;
import com.board.domain.comment.dto.response.CommentResponse;
import com.board.domain.member.dto.response.MemberResponse;
import com.board.support.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommentControllerDocsTest extends RestDocsSupport {

    public static final String PATH = "/api/v1/comments";

    private final CommentService commentService = mock(CommentService.class);

    @Override
    protected Object initController() {
        return new CommentController(commentService);
    }

    @Test
    @DisplayName("댓글 등록 API")
    void createComment() throws Exception {
        // given
        CommentRequest request = CommentRequest.builder()
                .articleId(1L)
                .content("댓글입니다.")
                .build();

        // when, then
        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andDo(document.document(
                        requestFields(
                                fieldWithPath("articleId").type(JsonFieldType.NUMBER)
                                        .description("게시글 ID"),
                                fieldWithPath("content").type(JsonFieldType.STRING)
                                        .description("댓글 내용")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("HTTP 상태 코드"),
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                        .description("요청 처리 결과"),
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
    @DisplayName("댓글 1건 조회 API")
    void getComment() throws Exception {
        // given
        MemberResponse memberResponse = new MemberResponse(1L, "khghouse@naver.com");
        CommentResponse response = new CommentResponse(1L, "댓글입니다.", LocalDateTime.now(), memberResponse);

        BDDMockito.given(commentService.getComment(any()))
                .willReturn(response);

        // when, then
        mockMvc.perform(get(PATH + "/{id}", 1L))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("id").description("댓글 ID")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("HTTP 상태 코드"),
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                        .description("요청 처리 결과"),
                                fieldWithPath("error").type(JsonFieldType.OBJECT)
                                        .description("에러 정보")
                                        .optional(),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터")
                                        .optional(),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("댓글 ID"),
                                fieldWithPath("data.content").type(JsonFieldType.STRING)
                                        .description("댓글 내용"),
                                fieldWithPath("data.createdDateTime").type(JsonFieldType.STRING)
                                        .description("등록일시 [yyyy-MM-dd HH:mm:ss]"),
                                fieldWithPath("data.member").type(JsonFieldType.OBJECT)
                                        .description("회원 정보"),
                                fieldWithPath("data.member.id").type(JsonFieldType.NUMBER)
                                        .description("회원 ID"),
                                fieldWithPath("data.member.email").type(JsonFieldType.STRING)
                                        .description("회원 이메일")
                        )
                ));
    }

    @Test
    @DisplayName("댓글 수정 API")
    void updateComment() throws Exception {
        // given
        CommentRequest request = CommentRequest.builder()
                .content("수정된 댓글입니다.")
                .build();

        // when, then
        mockMvc.perform(put(PATH + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("id").description("댓글 ID")
                        ),
                        requestFields(
                                fieldWithPath("content").type(JsonFieldType.STRING)
                                        .description("댓글 내용"),
                                fieldWithPath("articleId").type(JsonFieldType.NUMBER)
                                        .description("게시글 ID")
                                        .ignored()
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("HTTP 상태 코드"),
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                        .description("요청 처리 결과"),
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
    @DisplayName("댓글 삭제 API")
    void deleteComment() throws Exception {
        // when, then
        mockMvc.perform(delete(PATH + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("id").description("댓글 ID")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("HTTP 상태 코드"),
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                        .description("요청 처리 결과"),
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
