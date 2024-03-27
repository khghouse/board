package com.board.api.auth;

import com.board.api.ApiResponse;
import com.board.api.auth.request.LoginRequest;
import com.board.api.auth.request.SingupRequest;
import com.board.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse login(@RequestBody @Validated LoginRequest request) {
        return ApiResponse.ok(authService.login(request.toServiceRequest()));
    }

    @PostMapping("/signup")
    public ApiResponse signup(@RequestBody @Validated SingupRequest request) {
        return ApiResponse.ok(authService.signup(request.toServiceRequest()));
    }

}
