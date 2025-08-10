package com.board.global.common.dto.page;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageServiceRequest {

    private static final Integer DEFAULT_PAGE_SIZE = 20;
    private static final String DEFAULT_SORT_DIRECTION = "desc";
    private static final String DEFAULT_SORT_BY_COLUMN = "id";

    private Integer pageNumber;
    private Integer pageSize;
    private String sortDirection;
    private String sortByColumn;

    @Builder(access = AccessLevel.PRIVATE)
    private PageServiceRequest(Integer pageNumber, Integer pageSize, String sortDirection, String sortByColumn) {
        this.pageNumber = (pageNumber != null) ? pageNumber - 1 : null;
        this.pageSize = (pageSize != null) ? pageSize : DEFAULT_PAGE_SIZE;
        this.sortDirection = (sortDirection != null) ? sortDirection : DEFAULT_SORT_DIRECTION;
        this.sortByColumn = (sortByColumn != null) ? sortByColumn : DEFAULT_SORT_BY_COLUMN;
    }

    public Pageable toPageable() {
        if (pageNumber == null) {
            return Pageable.unpaged(Sort.by(Sort.Direction.DESC, "id"));
        }
        return PageRequest.of(pageNumber, pageSize, Sort.Direction.fromString(sortDirection), sortByColumn);
    }

    public static PageServiceRequest of(Integer pageNumber, Integer pageSize, String sortDirection, String sortByColumn) {
        return PageServiceRequest.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .sortDirection(sortDirection)
                .sortByColumn(sortByColumn)
                .build();
    }

    public static PageServiceRequest withDefault() {
        return PageServiceRequest.builder()
                .build();
    }

    public static PageServiceRequest withPageAndSortDirection(Integer pageNumber, Integer pageSize, String sortDirection) {
        return PageServiceRequest.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .sortDirection(sortDirection)
                .build();
    }

    public static PageServiceRequest withPageNumber(Integer pageNumber) {
        return PageServiceRequest.builder()
                .pageNumber(pageNumber)
                .build();
    }

    public static PageServiceRequest withPageNumberAndSortByColumn(Integer pageNumber, String sortByColumn) {
        return PageServiceRequest.builder()
                .pageNumber(pageNumber)
                .sortByColumn(sortByColumn)
                .build();
    }

    public static PageServiceRequest withPageNumberAndSortDirection(Integer pageNumber, String sortDirection) {
        return PageServiceRequest.builder()
                .pageNumber(pageNumber)
                .sortDirection(sortDirection)
                .build();
    }

}
