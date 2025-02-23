package com.board.api.comment;

import com.board.api.comment.request.CommentRequest;
import com.board.support.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

class CommentControllerTest extends ControllerTestSupport {

    @Test
    @DisplayName("댓글을 등록하고 정상 응답한다.")
    void createComment() throws Exception {
        // given
        CommentRequest request = CommentRequest.builder()
                .content("댓글입니다.")
                .build();

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/articles/{articleId}/comments", 1L)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("댓글을 등록할 때 내용은 필수입니다.")
    void createCommentWithoutContent() throws Exception {
        // given
        CommentRequest request = CommentRequest.builder()
                .build();

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/articles/{articleId}/comments", 1L)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("댓글 내용을 입력해 주세요."));
    }

    @Test
    @DisplayName("댓글을 등록할 때 내용이 공백이면 에러를 응답한다.")
    void createCommentBlankContent() throws Exception {
        // given
        CommentRequest request = CommentRequest.builder()
                .content("     ")
                .build();

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/articles/{articleId}/comments", 1L)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("댓글 내용을 입력해 주세요."));
    }

}