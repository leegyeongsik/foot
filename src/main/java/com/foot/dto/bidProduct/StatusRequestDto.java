package com.foot.dto.bidProduct;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StatusRequestDto {
    private List<Long> productIds;
    private int status;
}
