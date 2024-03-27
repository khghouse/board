package com.board.api.auth.request;

import com.board.service.auth.request.SignupServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SingupRequest {

    @NotBlank(message = "이메일을 입력해 주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    private String password;

    public SignupServiceRequest toServiceRequest() {
        return SignupServiceRequest.builder()
                .email(email)
                .password(password)
                .build();
    }

}
