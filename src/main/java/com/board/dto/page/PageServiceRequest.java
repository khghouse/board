package com.board.dto.page;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@NoArgsConstructor
public class PageServiceRequest {

    private static final Integer DEFAULT_PAGE_SIZE = 20;
    private static final String DEFAULT_DIRECTION = "desc";
    private static final String DEFAULT_PROPERTY = "id";

    private Integer pageNumber;
    private Integer pageSize;
    private String direction;
    private String property;

    @Builder
    private PageServiceRequest(Integer pageNumber, Integer pageSize, String direction, String property) {
        this.pageNumber = (pageNumber != null) ? pageNumber - 1 : null;
        this.pageSize = (pageSize != null) ? pageSize : DEFAULT_PAGE_SIZE;
        this.direction = (direction != null) ? direction : DEFAULT_DIRECTION;
        this.property = (property != null) ? property : DEFAULT_PROPERTY;
    }

    public Pageable toPageable() {
        if (pageNumber == null) {
            return Pageable.unpaged(Sort.by(Sort.Direction.DESC, "id"));
        }
        return PageRequest.of(pageNumber, pageSize, Sort.Direction.fromString(direction), property);
    }

}
