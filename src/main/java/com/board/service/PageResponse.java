package com.board.service;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PageResponse {

    private final PageInfomation pageInfomation;
    private final List<?> contents;

    @Builder
    private PageResponse(PageInfomation pageInfomation, List<?> contents) {
        this.pageInfomation = pageInfomation;
        this.contents = contents;
    }

    public static PageResponse of(Page<?> page, List<?> dtoContents) {
        return PageResponse.builder()
                .pageInfomation(PageInfomation.of(page.getNumber(), page.getTotalPages(), page.getTotalElements(), page.isLast()))
                .contents(dtoContents)
                .build();
    }

}
