package com.board.docs.member;

import com.board.api.member.MemberController;
import com.board.api.member.request.MemberRequest;
import com.board.docs.RestDocsSupport;
import com.board.service.member.MemberService;
import com.board.service.member.response.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberControllerDocsTest extends RestDocsSupport {

    private final MemberService memberService = mock(MemberService.class);

    @Override
    protected Object initController() {
        return new MemberController(memberService);
    }

    @Test
    @DisplayName("회원 등록 API")
    void postMember() throws Exception {
        // given
        MemberRequest request = MemberRequest.builder()
                .email("khghouse@daum.net")
                .password("Khghouse12!@")
                .build();

        BDDMockito.given(memberService.postMember(any()))
                .willReturn(MemberResponse.builder()
                        .id(1L)
                        .email("khghouse@daum.net")
                        .build());

        // when, then
        mockMvc.perform(post("/api/v1/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document.document(
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .description("비밀번호")
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
                                        .description("회원 ID"),
                                fieldWithPath("data.email").type(JsonFieldType.STRING)
                                        .description("이메일")
                        )
                ));
    }

}
