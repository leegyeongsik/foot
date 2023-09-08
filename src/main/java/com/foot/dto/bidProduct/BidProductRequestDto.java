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
//    private String originalFileName; // 원본 파일 이름
//    private String storedFileName; // 서버 저장용 파일 이름
//    private int fileAttached; // 파일 첨부 여부 (첨부 1,  미첨부 0)

    private String brand;
}
