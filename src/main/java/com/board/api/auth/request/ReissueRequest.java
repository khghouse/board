package com.board.api.auth.request;

import com.board.service.auth.request.ReissueServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReissueRequest {

    @NotBlank(message = "액세스 토큰을 입력해 주세요.")
    private String accessToken;

    @NotBlank(message = "리프레쉬 토큰을 입력해 주세요.")
    private String refreshToken;

    public ReissueServiceRequest toServiceRequest() {
        return ReissueServiceRequest.of(accessToken, refreshToken);
    }

}
