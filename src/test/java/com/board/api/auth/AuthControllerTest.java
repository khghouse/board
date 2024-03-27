package com.board.api.auth;

import com.board.api.ControllerTestSupport;
import com.board.api.auth.request.LoginRequest;
import com.board.api.auth.request.SingupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends ControllerTestSupport {

    @Test
    @DisplayName("로그인 후 정상 응답한다.")
    void login() throws Exception {
        // given
        LoginRequest request = LoginRequest.builder()
                .email("khghouse@daum.net")
                .password("Khghouse12!@")
                .build();

        // when, then
        mockMvc.perform(post("/api/v1/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인 시 이메일은 필수입니다.")
    void loginWithoutEmail() throws Exception {
        // given
        LoginRequest request = LoginRequest.builder()
                .password("Khghouse12!@")
                .build();

        // when, then
        mockMvc.perform(post("/api/v1/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("이메일을 입력해 주세요."));
    }

    @Test
    @DisplayName("로그인 시 이메일이 공백이면 클라이언트 에러를 응답한다.")
    void loginBlankTitle() throws Exception {
        // given
        LoginRequest request = LoginRequest.builder()
                .email("     ")
                .password("Khghouse12!@")
                .build();

        // when, then
        mockMvc.perform(post("/api/v1/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("이메일을 입력해 주세요."));
    }

    @Test
    @DisplayName("로그인 시 비밀번호는 필수입니다.")
    void loginWithoutPassword() throws Exception {
        // given
        LoginRequest request = LoginRequest.builder()
                .email("khghouse@daum.net")
                .build();

        // when, then
        mockMvc.perform(post("/api/v1/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("비밀번호를 입력해 주세요."));
    }

    @Test
    @DisplayName("로그인 시 비밀번호가 공백이면 클라이언트 에러를 응답한다.")
    void loginBlankPassword() throws Exception {
        // given
        LoginRequest request = LoginRequest.builder()
                .email("khghouse@daum.net")
                .password("     ")
                .build();

        // when, then
        mockMvc.perform(post("/api/v1/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("비밀번호를 입력해 주세요."));
    }


    @Test
    @DisplayName("회원을 등록하고 정상 응답한다.")
    void signup() throws Exception {
        // given
        SingupRequest request = SingupRequest.builder()
                .email("khghouse@daum.net")
                .password("Khghouse12!@")
                .build();

        // when, then
        mockMvc.perform(post("/api/v1/auth/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원을 등록할 때 이메일은 필수입니다.")
    void signupWithoutTitle() throws Exception {
        // given
        SingupRequest request = SingupRequest.builder()
                .password("Khghouse12!@")
                .build();

        // when, then
        mockMvc.perform(post("/api/v1/auth/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("이메일을 입력해 주세요."));
    }

    @Test
    @DisplayName("회원을 등록할 때 이메일이 공백이면 클라이언트 에러를 응답한다.")
    void signupBlankTitle() throws Exception {
        // given
        SingupRequest request = SingupRequest.builder()
                .email("     ")
                .password("Khghouse12!@")
                .build();

        // when, then
        mockMvc.perform(post("/api/v1/auth/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("이메일을 입력해 주세요."));
    }

    @Test
    @DisplayName("회원을 등록할 때 비밀번호는 필수입니다.")
    void signupWithoutPassword() throws Exception {
        // given
        SingupRequest request = SingupRequest.builder()
                .email("khghouse@daum.net")
                .build();

        // when, then
        mockMvc.perform(post("/api/v1/auth/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("비밀번호를 입력해 주세요."));
    }

    @Test
    @DisplayName("회원을 등록할 때 비밀번호가 공백이면 클라이언트 에러를 응답한다.")
    void signupBlankPassword() throws Exception {
        // given
        SingupRequest request = SingupRequest.builder()
                .email("khghouse@daum.net")
                .password("     ")
                .build();

        // when, then
        mockMvc.perform(post("/api/v1/auth/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("비밀번호를 입력해 주세요."));
    }

}