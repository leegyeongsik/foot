package com.foot.dto.bidProduct;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

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

    private MultipartFile bidProductFile;

    private String brand;
}
