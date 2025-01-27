package com.board.service.auth.request;

import com.board.domain.member.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthServiceRequest {

    private String email;
    private String password;

    @SuppressWarnings("unused")
    @Builder(access = AccessLevel.PRIVATE)
    private AuthServiceRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static AuthServiceRequest of(String email, String password) {
        return AuthServiceRequest.builder()
                .email(email)
                .password(password)
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .deleted(false)
                .build();
    }

}
