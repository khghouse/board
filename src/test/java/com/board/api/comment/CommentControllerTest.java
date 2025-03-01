package com.board.api.comment;

import com.board.api.comment.request.CommentRequest;
import com.board.support.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

class CommentControllerTest extends ControllerTestSupport {

    public static final String PATH = "/api/v1/comments";

    @Test
    @DisplayName("댓글을 등록하고 정상 응답한다.")
    void createComment() throws Exception {
        // given
        CommentRequest request = CommentRequest.builder()
                .articleId(1L)
                .content("댓글입니다.")
                .build();

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post(PATH)
                        .with(csrf())
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
                .articleId(1L)
                .build();

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post(PATH)
                        .with(csrf())
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
                .articleId(1L)
                .content("     ")
                .build();

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post(PATH)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("댓글 내용을 입력해 주세요."));
    }

    @Test
    @DisplayName("댓글을 등록할 때 게시글 ID는 필수입니다.")
    void createCommentWithoutArticleId() throws Exception {
        // given
        CommentRequest request = CommentRequest.builder()
                .content("댓글입니다.")
                .build();

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post(PATH)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("게시글 ID는 필수입니다."));
    }

    @Test
    @DisplayName("댓글을 등록할 때 게시글 ID는 0보다 커야 합니다.")
    void createCommentInvalidArticleId() throws Exception {
        // given
        CommentRequest request = CommentRequest.builder()
                .articleId(-1L)
                .content("댓글입니다.")
                .build();

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post(PATH)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("게시글 ID는 0보다 큰 숫자여야 합니다."));
    }

    @Test
    @DisplayName("댓글을 등록할 때 게시글 ID는 0보다 커야 합니다.")
    void createCommentInvalidArticleId2() throws Exception {
        // given
        CommentRequest request = CommentRequest.builder()
                .articleId(0L)
                .content("댓글입니다.")
                .build();

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/comments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("게시글 ID는 0보다 큰 숫자여야 합니다."));
    }

    @Test
    @DisplayName("댓글 1건을 조회하고 정상 응답한다.")
    void getComment() throws Exception {
        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get(PATH + "/{id}", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("댓글 1건을 조회할 때 ID값이 숫자 타입이 아니면 에러를 응답한다.")
    void getCommentIdTypeMismatch() throws Exception {
        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get(PATH + "/{id}", "1L")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("요청 파라미터 타입이 올바르지 않습니다."));
    }

    @Test
    @DisplayName("댓글을 수정하고 정상 응답한다.")
    void updateComment() throws Exception {
        // given
        CommentRequest request = CommentRequest.builder()
                .content("수정 댓글입니다.")
                .build();

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.put(PATH + "/{id}", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("댓글을 수정할 때 내용은 필수입니다.")
    void updateCommentWithoutContent() throws Exception {
        // given
        CommentRequest request = CommentRequest.builder()
                .build();

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.put(PATH + "/{id}", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("댓글 내용을 입력해 주세요."));
    }

    @Test
    @DisplayName("댓글을 수정할 때 내용이 공백이면 에러를 응답한다.")
    void updateCommentBlankContent() throws Exception {
        // given
        CommentRequest request = CommentRequest.builder()
                .content("     ")
                .build();

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.put(PATH + "/{id}", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("댓글 내용을 입력해 주세요."));
    }

    @Test
    @DisplayName("댓글을 수정할 때 ID값이 숫자 타입이 아니면 에러를 응답한다.")
    void updateCommentIdTypeMismatch() throws Exception {
        // given
        CommentRequest request = CommentRequest.builder()
                .content("수정 댓글입니다.")
                .build();

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.put(PATH + "/{id}", "1L")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("요청 파라미터 타입이 올바르지 않습니다."));
    }

    @Test
    @DisplayName("댓글을 삭제할 때 ID값이 숫자 타입이 아니면 에러를 응답한다.")
    void deleteComment() throws Exception {
        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.delete(PATH + "/{id}", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    @DisplayName("댓글을 삭제할 때 ID값이 숫자 타입이 아니면 에러를 응답한다.")
    void deleteCommentIdTypeMismatch() throws Exception {
        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.delete(PATH + "/{id}", "1L")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("요청 파라미터 타입이 올바르지 않습니다."));
    }

}