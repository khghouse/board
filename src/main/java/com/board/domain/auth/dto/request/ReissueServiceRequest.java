package com.board.domain.auth.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReissueServiceRequest {

    private String accessToken;
    private String refreshToken;

    @SuppressWarnings("unused")
    @Builder(access = AccessLevel.PRIVATE)
    private ReissueServiceRequest(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static ReissueServiceRequest of(String accessToken, String refreshToken) {
        return ReissueServiceRequest.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}
