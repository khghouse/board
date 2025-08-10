package com.board.global.common.dto.page;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PageRequest {

    private Integer pageNumber;
    private Integer pageSize;
    private String sortDirection;
    private String sortByColumn;

    public PageServiceRequest toServiceRequest() {
        return PageServiceRequest.of(pageNumber, pageSize, sortDirection, sortByColumn);
    }

}
