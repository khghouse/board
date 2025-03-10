package com.board.util;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CommonUtil {

    public static <T, R> List<R> mapperToList(List<T> list, Function<T, R> function) {
        return list.stream()
                .map(function::apply)
                .collect(Collectors.toList());
    }

    public static <T, M, R> List<R> mapperToList(List<T> list, Function<T, M> firstFunction, Function<M, R> secondFunction) {
        return list.stream()
                .map(firstFunction::apply)
                .map(secondFunction::apply)
                .collect(Collectors.toList());
    }

}
