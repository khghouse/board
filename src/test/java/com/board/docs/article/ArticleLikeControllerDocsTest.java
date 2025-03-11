package com.board.docs.article;

import com.board.api.article.ArticleLikeController;
import com.board.dto.page.PageInformation;
import com.board.dto.page.PageResponseWithExtraData;
import com.board.service.article.ArticleLikeService;
import com.board.service.article.response.ArticleIdResponse;
import com.board.service.member.response.MemberResponse;
import com.board.support.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
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

    @Test
    @DisplayName("특정 게시글을 좋아요한 회원 목록 조회 API")
    void getLikedMembers() throws Exception {
        // given
        MemberResponse memberResponse1 = toResponse(1L, "khghouse@naver.com");
        MemberResponse memberResponse2 = toResponse(2L, "khghouse@daum.net");

        PageResponseWithExtraData response = PageResponseWithExtraData.builder()
                .pageInformation(PageInformation.of(1, 1, 2, true))
                .extraData(new ArticleIdResponse(1L))
                .contents(List.of(memberResponse2, memberResponse1))
                .build();

        BDDMockito.given(articleLikeService.getLikedMembers(anyLong(), any()))
                .willReturn(response);

        // when, then
        mockMvc.perform(get(PATH + "/members", 1L)
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
                                        .optional(),
                                fieldWithPath("data.pageInformation").type(JsonFieldType.OBJECT)
                                        .description("페이지 정보"),
                                fieldWithPath("data.pageInformation.pageNumber").type(JsonFieldType.NUMBER)
                                        .description("현재 페이지 번호"),
                                fieldWithPath("data.pageInformation.totalPages").type(JsonFieldType.NUMBER)
                                        .description("총 페이지"),
                                fieldWithPath("data.pageInformation.totalElements").type(JsonFieldType.NUMBER)
                                        .description("총 데이터 수"),
                                fieldWithPath("data.pageInformation.isLast").type(JsonFieldType.BOOLEAN)
                                        .description("마지막 페이지 여부 (true : 마지막 페이지, false : 마지막 페이지 아님)"),
                                fieldWithPath("data.articleId").type(JsonFieldType.NUMBER)
                                        .description("게시글 ID"),
                                fieldWithPath("data.contents[]").type(JsonFieldType.ARRAY)
                                        .description("데이터 목록")
                                        .optional(),
                                fieldWithPath("data.contents[].id").type(JsonFieldType.NUMBER)
                                        .description("회원 ID"),
                                fieldWithPath("data.contents[].email").type(JsonFieldType.STRING)
                                        .description("회원 이메일")
                        )
                ));
    }

    private MemberResponse toResponse(Long id, String email) {
        return MemberResponse.builder()
                .id(id)
                .email(email)
                .build();
    }

}
