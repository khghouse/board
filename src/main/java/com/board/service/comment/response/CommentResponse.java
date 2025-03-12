package com.board.service.comment.response;

import com.board.domain.comment.Comment;
import com.board.service.member.response.MemberResponse;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String content,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdDateTime,
        MemberResponse member
) {
    public static CommentResponse of(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedDateTime(),
                MemberResponse.of(comment.getMember())
        );
    }
}
