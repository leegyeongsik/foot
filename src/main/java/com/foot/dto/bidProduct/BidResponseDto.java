package com.foot.dto.bidProduct;

import com.foot.entity.Bid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BidResponseDto {

    private Long id;
    private Long bidPrice;
    private Long bidProductId;
    private String username;

    // user도 들어가야 함

    public BidResponseDto(Bid bid) {
        id = bid.getId();
        bidPrice = bid.getBidPrice();
        bidProductId = bid.getBidProduct().getId();
        username = bid.getUser().getName();
    }
}
