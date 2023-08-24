package com.foot.dto.products;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
@Setter
public class UpdateProductResponseDto {
    String name;
    String description;
    Long price;
    Long totalAmount;
    HashMap<Integer, ArrayList<Long>> ProductColorAmount;
    HashMap<Integer, ArrayList<Long>> ProductSizeInfo;

}
