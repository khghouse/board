package com.board.api.auth;

import com.board.api.ApiResponse;
import com.board.api.auth.request.AuthRequest;
import com.board.api.auth.request.ReissueRequest;
import com.board.exception.UnauthorizedException;
import com.board.provider.JwtTokenProvider;
import com.board.service.auth.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
    public ApiResponse signup(@RequestBody @Validated AuthRequest request) {
        authService.signup(request.toServiceRequest());
        return ApiResponse.ok();
    }

    @PostMapping("/login")
    public ApiResponse login(@RequestBody @Validated AuthRequest request) {
        return ApiResponse.ok(authService.login(request.toServiceRequest()));
    }

    @PostMapping("/token/reissue")
    public ApiResponse reissueToken(@RequestBody @Validated ReissueRequest request) {
        return ApiResponse.ok(authService.reissueToken(request.toServiceRequest()));
    }

    @PostMapping("/logout")
    public ApiResponse logout(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resolveToken(request);
        if (accessToken == null) {
            throw new UnauthorizedException("인증되지 않은 요청입니다.");
        }
        authService.logout(accessToken);
        return ApiResponse.ok();
    }

}
