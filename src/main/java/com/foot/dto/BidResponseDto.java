package com.foot.dto;

import com.foot.entity.Bid;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BidResponseDto {

    private Long id;
    private Long bidPrice;
    private Long bidProductId;

    // user도 들어가야 함

    public BidResponseDto(Bid bid) {
        id = bid.getId();
        bidPrice = bid.getBidPrice();
        bidProductId = bid.getBidProduct().getId();
    }
}
