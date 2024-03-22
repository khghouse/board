package com.board.service.auth.request;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginServiceRequest {

    private String email;
    private String password;

}
