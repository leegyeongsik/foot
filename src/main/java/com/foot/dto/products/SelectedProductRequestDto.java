package com.foot.dto.products;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SelectedProductRequestDto {
    private List<Long> productIds;
}
