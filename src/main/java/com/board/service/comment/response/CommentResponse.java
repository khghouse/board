package com.board.service.comment.response;

import com.board.domain.comment.Comment;
import com.board.service.member.response.MemberResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentResponse {

    private final Long id;
    private final String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime createdDateTime;

    private final MemberResponse member;

    public static CommentResponse of(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdDateTime(comment.getCreatedDateTime())
                .member(MemberResponse.of(comment.getMember()))
                .build();
    }

}
