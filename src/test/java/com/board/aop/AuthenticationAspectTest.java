package com.board.aop;

import com.board.support.IntegrationTestSupport;
import com.board.provider.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class AuthenticationAspectTest extends IntegrationTestSupport {

    private static final String TOKEN_TYPE = "Bearer ";

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .build();
    }

//    @Test
//    @DisplayName("API 호출 전, 액세스 토큰을 검증한다.")
//    void authentication() throws Exception {
//        // given
//        String accessToken = jwtTokenProvider.createAccessToken(1L);
//
//        // when, then
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/articles")
//                        .header("Authorization", TOKEN_TYPE + accessToken))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("액세스 토큰이 null이면 401 에러가 발생한다.")
//    void authenticationTokenNull() throws Exception {
//        // when, then
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/articles"))
//                .andExpect(status().isUnauthorized())
//                .andExpect(jsonPath("$.status").value("401"))
//                .andExpect(jsonPath("$.data").isEmpty())
//                .andExpect(jsonPath("$.error.message").value("인증 정보가 존재하지 않습니다."));
//    }
//
//    @Test
//    @DisplayName("만료된 액세스 토큰은 401 에러가 발생한다.")
//    void authenticationTokenExpiration() throws Exception {
//        // given
//        String expiredAccessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2UiLCJleHAiOjE3MTA0NjcyOTF9.U-XaDqdC9uDXHNw-jD0EyoWXm3fQM9sVjQTAq5893g8";
//
//        // when, then
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/articles")
//                        .header("Authorization", TOKEN_TYPE + expiredAccessToken))
//                .andExpect(status().isUnauthorized())
//                .andExpect(jsonPath("$.status").value("401"))
//                .andExpect(jsonPath("$.data").isEmpty())
//                .andExpect(jsonPath("$.error.message").value("인증 정보가 유효하지 않습니다."));
//    }

}