package com.board.global.common.dto;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;

@Getter
public class RequestContext {

    private final String method;
    private final String uri;
    private final String queryString;
    private final String fullUri;

    public RequestContext(HttpServletRequest request) {
        this.method = request.getMethod();
        this.uri = request.getRequestURI();
        this.queryString = request.getQueryString();
        this.fullUri = createFullUri(request);
    }

    private String createFullUri(HttpServletRequest request) {
        if (request.getQueryString() != null) {
            return request.getRequestURI() + "?" + request.getQueryString();
        }
        return request.getRequestURI();
    }

}
