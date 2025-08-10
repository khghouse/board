package com.board.global.common.util;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CommonUtil {

    public static <T, R> List<R> mapperToList(List<T> list, Function<T, R> function) {
        return list.stream()
                .map(function)
                .collect(Collectors.toList());
    }

    public static <T, M, R> List<R> mapperToList(List<T> list, Function<T, M> firstFunction, Function<M, R> secondFunction) {
        return list.stream()
                .map(firstFunction)
                .map(secondFunction)
                .collect(Collectors.toList());
    }

    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip.split(",")[0].trim(); // 첫 번째 IP가 실제 클라이언트 IP
        }
        return request.getRemoteAddr();
    }

}
