package com.board.service.member.request;

import com.board.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberServiceRequest {

    private String email;
    private String password;

    @Builder
    private MemberServiceRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .deleted(false)
                .build();
    }

}
