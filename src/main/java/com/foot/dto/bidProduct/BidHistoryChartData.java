package com.foot.dto.bidProduct;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class BidHistoryChartData {
    private LocalDateTime date;
    private Long count;

    public BidHistoryChartData(LocalDateTime createdAt, Long count) {
        this.date = createdAt;
        this.count = count;
    }
}
