package com.board.service.member.response;

import com.board.domain.member.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberResponse {

    private final Long id;
    private final String email;

    @JsonIgnore
    private final String password;

    @JsonIgnore
    private final Boolean deleted;

    @JsonIgnore
    private final LocalDateTime createdDateTime;

    @JsonIgnore
    private final LocalDateTime modifiedDateTime;

    public static MemberResponse of(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .password(member.getPassword())
                .deleted(member.getDeleted())
                .createdDateTime(member.getCreatedDateTime())
                .modifiedDateTime(member.getModifiedDateTime())
                .build();
    }

}
