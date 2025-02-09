package com.board.api.auth.request;

import com.board.service.auth.request.AuthServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthRequest {

    @NotBlank(message = "이메일을 입력해 주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    private String password;

    public AuthServiceRequest toServiceRequest() {
        return AuthServiceRequest.of(email, password);
    }

}
