package com.board.domain.member.dto.request;

import com.board.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberServiceRequest {

    private String email;
    private String password;

    @Builder(access = AccessLevel.PRIVATE)
    public MemberServiceRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static MemberServiceRequest of(String email, String password) {
        return MemberServiceRequest.builder()
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
