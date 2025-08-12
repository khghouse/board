package com.board.domain.auth.controller;

import com.board.domain.auth.dto.request.AuthRequest;
import com.board.domain.auth.dto.request.ReissueRequest;
import com.board.domain.auth.service.AuthService;
import com.board.global.common.dto.ApiResponse;
import com.board.global.common.enumeration.ErrorCode;
import com.board.global.common.exception.UnauthorizedException;
import com.board.global.security.JwtToken;
import com.board.global.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signup")
    public ApiResponse<Void> signup(@RequestBody @Validated AuthRequest request) {
        authService.signup(request.toServiceRequest());
        return ApiResponse.ok();
    }

    @PostMapping("/login")
    public ApiResponse<JwtToken> login(@RequestBody @Validated AuthRequest request) {
        return ApiResponse.ok(authService.login(request.toServiceRequest()));
    }

    @PostMapping("/token/reissue")
    public ApiResponse<JwtToken> reissueToken(@RequestBody @Validated ReissueRequest request) {
        return ApiResponse.ok(authService.reissueToken(request.toServiceRequest()));
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resolveToken(request);
        if (!StringUtils.hasText(accessToken)) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        }
        authService.logout(accessToken);
        return ApiResponse.ok();
    }

}
