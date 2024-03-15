package com.board.api.auth;

import com.board.api.ApiResponse;
import com.board.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ApiResponse postLogin() {
        return ApiResponse.ok(jwtTokenProvider.createAccessToken());
    }

}
