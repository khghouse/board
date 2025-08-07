package com.board.docs.auth;

import com.board.api.auth.AuthController;
import com.board.api.auth.request.AuthRequest;
import com.board.support.RestDocsSupport;
import com.board.dto.jwt.JwtToken;
import com.board.provider.JwtTokenProvider;
import com.board.service.auth.AuthService;
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

public class AuthControllerDocsTest extends RestDocsSupport {

    private final AuthService authService = mock(AuthService.class);
    private final JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);

    @Override
    protected Object initController() {
        return new AuthController(authService, jwtTokenProvider);
    }

    @Test
    @DisplayName("로그인 API")
    void login() throws Exception {
        // given
        AuthRequest request = AuthRequest.builder()
                .email("khghouse@daum.net")
                .password("Khghouse12!@")
                .build();

        BDDMockito.given(authService.login(any()))
                .willReturn(JwtToken.builder()
                        .accessToken("json.web.Accesstoken")
                        .refreshToken("json.web.Refreshtoken")
                        .build());

        // when, then
        mockMvc.perform(post("/api/v1/auth/login")
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
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                        .description("요청 처리 결과"),
                                fieldWithPath("error").type(JsonFieldType.OBJECT)
                                        .description("에러 정보")
                                        .optional(),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터")
                                        .optional(),
                                fieldWithPath("data.accessToken").type(JsonFieldType.STRING)
                                        .description("액세스 토큰"),
                                fieldWithPath("data.refreshToken").type(JsonFieldType.STRING)
                                        .description("리프레쉬 토큰")
                        )
                ));
    }

    @Test
    @DisplayName("회원 가입 API")
    void signup() throws Exception {
        // given
        AuthRequest request = AuthRequest.builder()
                .email("khghouse@daum.net")
                .password("Khghouse12!@")
                .build();

        // when, then
        mockMvc.perform(post("/api/v1/auth/signup")
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
