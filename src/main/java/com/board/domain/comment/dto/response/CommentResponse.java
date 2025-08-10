package com.board.domain.comment.dto.response;

import com.board.domain.comment.entity.Comment;
import com.board.domain.member.dto.response.MemberResponse;
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
