package com.board.global.common.filter;

import com.board.global.common.dto.RequestContext;
import com.board.global.common.matcher.LogExclusionMatcher;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

/**
 * 모든 HTTP 요청의 메타 정보를 로깅하는 필터입니다.
 * HTTP 메서드, URI, 응답 코드, 처리 시간 등을 기록합니다.
 */
@Slf4j
@Component
public class AccessLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (LogExclusionMatcher.shouldSkipLogging(request)) {
            return;
        }

        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);
        long startTime = System.currentTimeMillis();

        try {
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            RequestContext requestContext = new RequestContext(request);
            int responseStatusCode = wrappedResponse.getStatus();
            log.info("[{}] {} [{}] ({}ms)", requestContext.getMethod(), requestContext.getFullUri(), HttpStatus.valueOf(responseStatusCode), duration);

            wrappedResponse.copyBodyToResponse();
        }
    }

}
