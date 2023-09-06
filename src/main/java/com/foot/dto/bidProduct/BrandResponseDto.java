package com.foot.dto.bidProduct;

import com.foot.entity.Brand;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrandResponseDto {

    private Long id;
    private String name;

    public BrandResponseDto(Brand brand) {
        this.id = brand.getId();
        this.name = brand.getName();
    }
}
