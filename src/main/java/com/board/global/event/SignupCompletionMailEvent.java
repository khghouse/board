package com.board.global.event;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SignupCompletionMailEvent {

    private String email;

    @SuppressWarnings("unused")
    @Builder(access = AccessLevel.PRIVATE)
    private SignupCompletionMailEvent(String email) {
        this.email = email;
    }

    public static SignupCompletionMailEvent create(String email) {
        return SignupCompletionMailEvent.builder()
                .email(email)
                .build();
    }

}
