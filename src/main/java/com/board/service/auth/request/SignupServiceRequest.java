package com.board.service.auth.request;

import com.board.domain.member.Member;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SignupServiceRequest {

    private String email;
    private String password;

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .deleted(false)
                .build();
    }

}
