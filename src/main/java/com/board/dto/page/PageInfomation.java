package com.board.dto.page;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PageInfomation {

    private final int pageNumber;
    private final int totalPages;
    private final long totalElements;
    private final Boolean isLast;

    public static PageInfomation of(int pageNumber, int totalPages, long totalElements, boolean isLast) {
        return PageInfomation.builder()
                .pageNumber(pageNumber + 1)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .isLast(isLast)
                .build();
    }

}
