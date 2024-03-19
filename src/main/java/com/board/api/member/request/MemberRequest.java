package com.board.api.member.request;

import com.board.service.member.request.MemberServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberRequest {

    @NotBlank(message = "이메일을 입력해 주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    private String password;

    public MemberServiceRequest toServiceRequest() {
        return MemberServiceRequest.builder()
                .email(email)
                .password(password)
                .build();
    }

}
