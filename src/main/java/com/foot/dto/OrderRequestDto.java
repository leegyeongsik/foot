package com.foot.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class OrderRequestDto  implements Serializable {
    private Long orderCartItemId;
    private Long orderPrice;
    private Long orderCount;
    private Long orderProductcolorId;
    private Long orderUserId;


}
