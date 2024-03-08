package com.board.api;

import com.board.service.PageServiceRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class PageRequest {

    private Integer pageNumber;
    private Integer pageSize;
    private String direction;
    private String property;

    @Builder
    private PageRequest(Integer pageNumber, Integer pageSize, String direction, String property) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.direction = direction;
        this.property = property;
    }

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
