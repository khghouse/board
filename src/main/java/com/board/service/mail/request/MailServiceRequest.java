package com.board.service.mail.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MailServiceRequest {

    private String email;

    @SuppressWarnings("unused")
    @Builder(access = AccessLevel.PRIVATE)
    private MailServiceRequest(String email) {
        this.email = email;
    }

    public static MailServiceRequest of(String email) {
        return MailServiceRequest.builder()
                .email(email)
                .build();
    }

}
