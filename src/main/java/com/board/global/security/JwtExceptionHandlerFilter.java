package com.board.global.security;

import com.board.global.common.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JwtExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            setErrorResponse(response, e.getJwtErrorCode());
        }
    }

    private void setErrorResponse(HttpServletResponse response, JwtErrorCode jwtErrorCode) {
        ApiResponse<Void> apiResponse = ApiResponse.jwtUnauthorized(jwtErrorCode);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(response.getWriter(), apiResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
