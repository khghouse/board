package com.board.api.article;

import com.board.support.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ArticleLikeControllerTest extends ControllerTestSupport {

    @Test
    @DisplayName("게시글 좋아요를 추가하고 정상 응답한다.")
    void like() throws Exception {
        // given
        Long articleId = 1L;

        // when, then
        mockMvc.perform(post("/articles/{id}/likes", articleId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시글 좋아요를 추가할 때 ID값이 숫자 타입이 아니면 에러를 응답한다.")
    void likeIdTypeMismatch() throws Exception {
        // when, then
        mockMvc.perform(post("/articles/{id}/likes", "1L")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("요청 파라미터 타입이 올바르지 않습니다."));
    }

    @Test
    @DisplayName("게시글 좋아요를 취소하고 정상 응답한다.")
    void unlike() throws Exception {
        // given
        Long articleId = 1L;

        // when, then
        mockMvc.perform(delete("/articles/{id}/likes", articleId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시글 좋아요를 취소할 때 ID값이 숫자 타입이 아니면 에러를 응답한다.")
    void unlikeIdTypeMismatch() throws Exception {
        // when, then
        mockMvc.perform(delete("/articles/{id}/likes", "1L")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("요청 파라미터 타입이 올바르지 않습니다."));
    }

    @Test
    @DisplayName("특정 게시글에 좋아요를 누른 회원 목록을 조회하고 정상 응답한다.")
    void getLikedMembers() throws Exception {
        // given
        Long articleId = 1L;

        // when, then
        mockMvc.perform(get("/articles/{id}/likes/members", articleId)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("특정 게시글에 좋아요를 누른 회원 목록을 조회할 때 ID값이 숫자 타입이 아니면 에러를 응답한다.")
    void getLikedMembersIdTypeMismatch() throws Exception {
        // when, then
        mockMvc.perform(get("/articles/{id}/likes/members", "1L")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("요청 파라미터 타입이 올바르지 않습니다."));
    }

    @Test
    @DisplayName("특정 회원이 좋아요한 게시글 목록을 조회하고 검증한다.")
    void getLikedArticles() throws Exception {
        // given
        Long memberId = 1L;

        // when, then
        mockMvc.perform(get("/members/{id}/likes/articles", memberId)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("특정 회원이 좋아요한 게시글 목록을 조회할 때 ID값이 숫자 타입이 아니면 에러를 응답한다.")
    void getLikedArticlesIdTypeMismatch() throws Exception {
        // when, then
        mockMvc.perform(get("/members/{id}/likes/articles", "1L")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").value("요청 파라미터 타입이 올바르지 않습니다."));
    }

}