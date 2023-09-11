package com.foot.dto.bidProduct;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class BidProductChartData {
    private LocalDateTime date;
    private Long count;

    public BidProductChartData(LocalDateTime date, Long count) {
        this.date = date;
        this.count = count;
    }

}
