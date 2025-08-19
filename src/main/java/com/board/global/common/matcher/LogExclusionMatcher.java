package com.board.global.common.matcher;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.regex.Pattern;

public class LogExclusionMatcher {

    private static final List<Pattern> EXCLUDED_PATTERNS = List.of(
            Pattern.compile("^/actuator/.*")
    );

    public static boolean shouldSkipLogging(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return EXCLUDED_PATTERNS.stream()
                .anyMatch(pattern -> pattern.matcher(uri).matches());
    }

}
