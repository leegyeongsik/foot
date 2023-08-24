package com.foot.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
public class BidProductRequestDto {

    private String name;
    private String description;
    private Long startPrice;
    private LocalDateTime expirationPeriod;
    private Long feetSize;
    private Long footSize;
    private String footPicture;
    private String brand;
}
