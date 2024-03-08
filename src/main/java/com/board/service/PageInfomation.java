package com.board.service;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PageInfomation {

    private final int pageNumber;
    private final int totalPages;
    private final long totalElements;
    private final Boolean isLast;

    @Builder
    private PageInfomation(int pageNumber, int totalPages, long totalElements, boolean isLast) {
        this.pageNumber = pageNumber;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.isLast = isLast;
    }

    public static PageInfomation of(int pageNumber, int totalPages, long totalElements, boolean isLast) {
        return PageInfomation.builder()
                .pageNumber(pageNumber + 1)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .isLast(isLast)
                .build();
    }

}
