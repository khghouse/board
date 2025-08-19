package com.board.global.common.filter;

import com.board.global.common.dto.RequestContext;
import com.board.global.common.matcher.LogExclusionMatcher;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * HTTP 요청 및 응답의 바디와 파라미터를 로깅하는 필터입니다.
 * 로컬, 개발, 테스트 환경에서는 전체 요청/응답을 로깅하며,
 * 운영 환경에서는 예외 발생 시에만 로깅합니다.
 */
@Slf4j
public abstract class AbstractRequestResponseLoggingFilter extends OncePerRequestFilter {

    private static final Set<String> MASKING_FIELDS = Set.of("accessToken", "refreshToken", "password");

    private final ObjectMapper objectMapper = new ObjectMapper();

    abstract boolean shouldLogRequestResponse(HttpServletRequest request, HttpServletResponse response);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (LogExclusionMatcher.shouldSkipLogging(request)) {
            return;
        }

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            if (shouldLogRequestResponse(request, response)) {
                logRequest(wrappedRequest);
                logResponse(wrappedResponse);
            }
            wrappedResponse.copyBodyToResponse();
        }
    }

    private void logRequest(ContentCachingRequestWrapper request) {
        String body = new String(request.getContentAsByteArray(), StandardCharsets.UTF_8);
        String prettyBody = toPrettyJson(body);
        RequestContext requestContext = new RequestContext(request);
        if (prettyBody.isEmpty()) {
            log.info(">>> {} {}", requestContext.getMethod(), requestContext.getFullUri());
        } else {
            log.info(">>> {} {}\n{}", requestContext.getMethod(), requestContext.getFullUri(), prettyBody);
        }
    }

    private void logResponse(ContentCachingResponseWrapper response) {
        String body = new String(response.getContentAsByteArray(), StandardCharsets.UTF_8);
        String prettyBody = toPrettyJson(body);

        if (prettyBody.isEmpty()) {
            log.info("<<< {}", HttpStatus.valueOf(response.getStatus()));
        } else {
            log.info("<<< {}\n{}", HttpStatus.valueOf(response.getStatus()), prettyBody);
        }
    }

    private String toPrettyJson(String json) {
        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            maskSensitiveFields(jsonNode);
            return jsonNode.toPrettyString();
        } catch (Exception e) {
            return json;
        }
    }

    private void maskSensitiveFields(JsonNode node) {
        if (node.isObject()) { // { ... } 형태일 경우
            ObjectNode objectNode = (ObjectNode) node; // JsonNode (Immutable) -> ObjectNode (Mutable)
            Iterator<Map.Entry<String, JsonNode>> fields = objectNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                String key = entry.getKey();
                JsonNode value = entry.getValue();

                if (value.isValueNode()) { // 단순 값 형태
                    if (MASKING_FIELDS.contains(key)) {
                        objectNode.put(key, "[MASKED]");
                    }
                } else { // { ... }, [ ... ]
                    if (value.isObject()) {
                        maskSensitiveFields(value);
                    } else if (value.isArray()) {
                        for (JsonNode item : value) {
                            maskSensitiveFields(item);
                        }
                    }
                }
            }
        } else if (node.isArray()) { // [ ... ] 형태일 경우
            for (JsonNode item : node) {
                maskSensitiveFields(item);
            }
        }
    }

}
