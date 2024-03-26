package com.board.service.member.request;

import com.board.domain.member.Member;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberServiceRequest {

    private String email;
    private String password;
    
    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .deleted(false)
                .build();
    }

    public Member toEntity(String encryptedPassword) {
        return Member.builder()
                .email(email)
                .password(encryptedPassword)
                .deleted(false)
                .build();
    }

}
