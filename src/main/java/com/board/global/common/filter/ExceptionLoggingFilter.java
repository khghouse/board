package com.board.global.common.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
public class ExceptionLoggingFilter extends AbstractRequestResponseLoggingFilter {

    @Override
    boolean shouldLogRequestResponse(HttpServletRequest request, HttpServletResponse response) {
        return response.getStatus() >= 400;
    }

}
