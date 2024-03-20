package com.board.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PageResponse {

    private final PageInfomation pageInfomation;
    private final List<?> contents;

    public static PageResponse of(Page<?> page, List<?> dtoContents) {
        return PageResponse.builder()
                .pageInfomation(PageInfomation.of(page.getNumber(), page.getTotalPages(), page.getTotalElements(), page.isLast()))
                .contents(dtoContents)
                .build();
    }

}
