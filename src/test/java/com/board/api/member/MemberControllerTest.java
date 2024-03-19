package com.board.api.member;

import com.board.api.ControllerTestSupport;
import com.board.api.member.request.MemberRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest extends ControllerTestSupport {

    @Test
    @DisplayName("회원 등록하고 정상 응답한다.")
    void postMember() throws Exception {
        // given
        MemberRequest request = MemberRequest.builder()
                .email("khghouse@daum.net")
                .password("Khghouse12!@")
                .build();

        // when, then
        mockMvc.perform(post("/api/v1/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원을 등록할 때 이메일은 필수입니다.")
    void postMemberWithoutTitle() throws Exception {
        // given
        MemberRequest request = MemberRequest.builder()
                .password("Khghouse12!@")
                .build();

        // when, then
        mockMvc.perform(post("/api/v1/members")
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
    void postMemberBlankTitle() throws Exception {
        // given
        MemberRequest request = MemberRequest.builder()
                .email("     ")
                .password("Khghouse12!@")
                .build();

        // when, then
        mockMvc.perform(post("/api/v1/members")
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
    void postMemberWithoutPassword() throws Exception {
        // given
        MemberRequest request = MemberRequest.builder()
                .email("khghouse@daum.net")
                .build();

        // when, then
        mockMvc.perform(post("/api/v1/members")
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
    void postMemberBlankPassword() throws Exception {
        // given
        MemberRequest request = MemberRequest.builder()
                .email("khghouse@daum.net")
                .password("     ")
                .build();

        // when, then
        mockMvc.perform(post("/api/v1/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("비밀번호를 입력해 주세요."));
    }

}