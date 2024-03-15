package com.board.api.auth;

import com.board.api.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends ControllerTestSupport {

    @Test
    @DisplayName("로그인 API 호출 결과 액세스 토큰을 획득한다.")
    void postLogin() throws Exception {
        // given
        BDDMockito.given(jwtTokenProvider.createAccessToken())
                .willReturn("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2UiLCJleHAiOjE3MTAyMDY3OTB9.QpDZXxqL0z955LHvFxBZ6mh4aFcXp8F_bVa8DBfEWPc");

        // when, then
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2UiLCJleHAiOjE3MTAyMDY3OTB9.QpDZXxqL0z955LHvFxBZ6mh4aFcXp8F_bVa8DBfEWPc"));
    }

}