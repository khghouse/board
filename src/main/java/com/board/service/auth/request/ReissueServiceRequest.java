package com.board.service.auth.request;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReissueServiceRequest {

    private String accessToken;
    private String refreshToken;

}
