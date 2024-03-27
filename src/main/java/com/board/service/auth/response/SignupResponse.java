package com.board.service.auth.response;

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
public class SignupResponse {

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

    private final String accessToken;

    public static SignupResponse of(Member member, String accessToken) {
        return SignupResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .password(member.getPassword())
                .deleted(member.getDeleted())
                .createdDateTime(member.getCreatedDateTime())
                .modifiedDateTime(member.getModifiedDateTime())
                .accessToken(accessToken)
                .build();
    }

}
