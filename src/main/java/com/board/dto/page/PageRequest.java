package com.board.dto.page;

import com.board.dto.page.PageServiceRequest;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PageRequest {

    private Integer pageNumber;
    private Integer pageSize;
    private String direction;
    private String property;

    public PageServiceRequest toServiceRequest() {
        return PageServiceRequest.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .direction(direction)
                .property(property)
                .build();
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setProperty(String property) {
        this.property = property;
    }

}
