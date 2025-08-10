package com.board.global.common.dto.page;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PageResponseWithExtraData<T> {

    private final PageInformation pageInformation;

    @JsonUnwrapped
    private final T extraData;
    private final List<?> contents;

    public static <T> PageResponseWithExtraData<T> of(Page<?> page, T extraData, List<?> dtoContents) {
        return PageResponseWithExtraData.<T>builder()
                .pageInformation(PageInformation.of(page.getNumber(), page.getTotalPages(), page.getTotalElements(), page.isLast()))
                .extraData(extraData)
                .contents(dtoContents)
                .build();
    }

}
