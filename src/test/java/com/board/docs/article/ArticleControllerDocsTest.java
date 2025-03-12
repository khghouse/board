package com.board.docs.article;

import com.board.api.article.ArticleController;
import com.board.api.article.request.ArticleRequest;
import com.board.dto.page.PageInformation;
import com.board.dto.page.PageResponse;
import com.board.service.article.ArticleService;
import com.board.service.article.response.ArticleDetailResponse;
import com.board.service.article.response.ArticleResponse;
import com.board.service.member.response.MemberResponse;
import com.board.support.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
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
    void createArticle() throws Exception {
        // given
        ArticleRequest request = ArticleRequest.builder()
                .title("게시글 제목입니다.")
                .content("게시글 내용입니다.")
                .build();

        BDDMockito.given(articleService.createArticle(any(), anyLong()))
                .willReturn(new ArticleResponse(1L, "게시글 제목입니다.", "게시글 내용입니다."));

        // when, then
        mockMvc.perform(post("/api/v1/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document.document(
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
                                        .description("응답 데이터")
                                        .optional(),
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
        MemberResponse memberResponse = new MemberResponse(1L, "khghouse@naver.com");
        ArticleDetailResponse response = new ArticleDetailResponse(1L, "게시글 제목입니다.", "게시글 내용입니다.", LocalDateTime.now(), LocalDateTime.now(), memberResponse);

        BDDMockito.given(articleService.getArticle(anyLong()))
                .willReturn(response);

        // when, then
        mockMvc.perform(get("/api/v1/articles/{id}", 1L))
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
                                        .optional(),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("게시글 ID"),
                                fieldWithPath("data.title").type(JsonFieldType.STRING)
                                        .description("게시글 제목"),
                                fieldWithPath("data.content").type(JsonFieldType.STRING)
                                        .description("게시글 내용"),
                                fieldWithPath("data.createdDateTime").type(JsonFieldType.STRING)
                                        .description("등록일시 [yyyy-MM-dd HH:mm:ss]"),
                                fieldWithPath("data.modifiedDateTime").type(JsonFieldType.STRING)
                                        .description("수정일시 [yyyy-MM-dd HH:mm:ss]"),
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
    @DisplayName("게시글 수정 API")
    void updateArticle() throws Exception {
        // given
        ArticleRequest request = ArticleRequest.builder()
                .title("게시글 제목")
                .content("게시글 내용.")
                .build();

        BDDMockito.given(articleService.updateArticle(any(), anyLong()))
                .willReturn(new ArticleResponse(1L, "게시글 제목", "게시글 내용"));

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
                                        .description("응답 데이터")
                                        .optional(),
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
        // when, then
        mockMvc.perform(delete("/api/v1/articles/{id}", 1L))
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

    @Test
    @DisplayName("게시글 리스트 조회 API")
    void getArticleList() throws Exception {
        // given
        MemberResponse memberResponse = new MemberResponse(1L, "khghouse@naver.com");

        ArticleDetailResponse articleResponse1 = new ArticleDetailResponse(1L, "게시글 제목입니다. 1", "게시글 내용입니다. 1", LocalDateTime.now(), LocalDateTime.now(), memberResponse);
        ArticleDetailResponse articleResponse2 = new ArticleDetailResponse(2L, "게시글 제목입니다. 2", "게시글 내용입니다. 2", LocalDateTime.now(), LocalDateTime.now(), memberResponse);
        ArticleDetailResponse articleResponse3 = new ArticleDetailResponse(3L, "게시글 제목입니다. 3", "게시글 내용입니다. 3", LocalDateTime.now(), LocalDateTime.now(), memberResponse);

        PageResponse response = PageResponse.builder()
                .pageInformation(PageInformation.of(1, 1, 3, true))
                .contents(List.of(articleResponse3, articleResponse2, articleResponse1))
                .build();

        BDDMockito.given(articleService.getArticleList(any()))
                .willReturn(response);

        // when, then
        mockMvc.perform(get("/api/v1/articles")
                        .param("pageNumber", "1")
                        .param("pageSize", "20")
                        .param("sortDirection", "desc")
                        .param("sortByColumn", "id"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document.document(
                        queryParameters(
                                parameterWithName("pageNumber").description("페이지 번호 (페이지 번호가 넘어올 경우에만 페이징 처리)")
                                        .optional(),
                                parameterWithName("pageSize").description("한 페이지당 데이터 수 - default = 20")
                                        .optional(),
                                parameterWithName("sortDirection").description("정렬 순서 [asc|desc] - default = desc")
                                        .optional(),
                                parameterWithName("sortByColumn").description("정렬 기준 컬럼 - default = id")
                                        .optional()
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
                                fieldWithPath("data.contents[]").type(JsonFieldType.ARRAY)
                                        .description("데이터 목록")
                                        .optional(),
                                fieldWithPath("data.contents[].id").type(JsonFieldType.NUMBER)
                                        .description("게시글 ID"),
                                fieldWithPath("data.contents[].title").type(JsonFieldType.STRING)
                                        .description("게시글 제목"),
                                fieldWithPath("data.contents[].content").type(JsonFieldType.STRING)
                                        .description("게시글 내용"),
                                fieldWithPath("data.contents[].createdDateTime").type(JsonFieldType.STRING)
                                        .description("등록일시 [yyyy-MM-dd HH:mm:ss]"),
                                fieldWithPath("data.contents[].modifiedDateTime").type(JsonFieldType.STRING)
                                        .description("수정일시 [yyyy-MM-dd HH:mm:ss]"),
                                fieldWithPath("data.contents[].member").type(JsonFieldType.OBJECT)
                                        .description("회원 정보"),
                                fieldWithPath("data.contents[].member.id").type(JsonFieldType.NUMBER)
                                        .description("회원 ID"),
                                fieldWithPath("data.contents[].member.email").type(JsonFieldType.STRING)
                                        .description("회원 이메일")
                        )
                ));
    }
    
}
