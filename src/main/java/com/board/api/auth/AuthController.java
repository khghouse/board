package com.board.api.auth;

import com.board.api.ApiResponse;
import com.board.api.auth.request.LoginRequest;
import com.board.service.auth.AuthService;
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

    @PostMapping("/login")
    public ApiResponse postLogin(@RequestBody @Validated LoginRequest request) {
        return ApiResponse.ok(authService.postLogin(request.toServiceRequest()));
    }

}
