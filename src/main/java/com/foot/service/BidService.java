package com.foot.service;

import com.foot.dto.bidProduct.BidProductRequestDto;
import com.foot.dto.bidProduct.BidProductResponseDto;
import com.foot.dto.bidProduct.BidRequestDto;
import com.foot.dto.bidProduct.BidResponseDto;
import com.foot.entity.*;
import com.foot.repository.BidHistoryRepository;
import com.foot.repository.BidProductRepository;
import com.foot.repository.BidRepository;
import com.foot.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BidService {

    private final BidProductRepository bidProductRepository;
    private final BidRepository bidRepository;
    private final BrandRepository brandRepository;
    private final BidHistoryRepository bidHistoryRepository;

    private final S3UploadService s3UploadService;


    //------------------- 경매 상품 관련 -------------------//

    // 경매 상품 생성
    public BidProductResponseDto createBidProduct(BidProductRequestDto requestDto, User user) throws IOException {
        Brand brand = brandRepository.findByName(requestDto.getBrand());

        // 현재 시간
        LocalDateTime currentTime = LocalDateTime.now();
        log.info("Current Time: {}", currentTime);

        // 만료 시간
        LocalDateTime expirationTime = requestDto.getExpirationPeriod();
        log.info("Expiration Time: {}", expirationTime);

        // 남은 시간 계산
        Duration duration = Duration.between(currentTime, expirationTime);
        log.info("Duration : {}", duration);

        // 남은 시간을 "X일 Y시간 Z분" 형식으로 포맷팅
        String remainingTime = formatRemainingTime(duration);
        log.info("Remaining Time: {}", remainingTime);

        BidProduct bidProduct = BidProduct.builder()
                .expirationPeriod(requestDto.getExpirationPeriod())
                .startPrice(requestDto.getStartPrice())
                .name(requestDto.getName())
                .description(requestDto.getDescription())
                .feetsize(requestDto.getFeetSize())
                .footsize(requestDto.getFootSize())
                .footpicture(s3UploadService.uploadImage(requestDto.getBidProductFile()))
                .brand(brand)
                .user(user)
                .build();

        bidProductRepository.save(bidProduct);
        return new BidProductResponseDto(bidProduct, remainingTime);
    }



    // 경매 상품 전체 조회
    @Transactional
    public List<BidProductResponseDto> getAllBidProduct() {
        List<BidProduct> bidProductList = bidProductRepository.findAll();
        List<BidProductResponseDto> bidProductResponseDtoList = new ArrayList<>();

        checkExpirationPeriod(bidProductList);

        for (int i = 0; i < bidProductList.size(); i++) {
            bidProductResponseDtoList.add(new BidProductResponseDto(bidProductList.get(i)));
        }

        return bidProductResponseDtoList;
    }

    // status가 0인(경매진행중인) 경매상품만 조회
    @Transactional
    public List<BidProductResponseDto> getActiveBidProducts() {
        List<BidProduct> activeBidProducts = bidProductRepository.findByStatus(0); // 상태가 0인 활성 경매 상품 조회
        checkExpirationPeriod(activeBidProducts);

        // 현재 시간
        LocalDateTime currentTime = LocalDateTime.now();

        List<BidProductResponseDto> bidProductResponses = activeBidProducts.stream()
                .map(bidProduct -> {
                    // 만료 시간
                    LocalDateTime expirationTime = bidProduct.getExpirationPeriod();

                    // 남은 시간 계산
                    Duration duration = Duration.between(currentTime, expirationTime);

                    // 남은 시간을 "X일 Y시간 Z분" 형식으로 포맷팅
                    String remainingTime = formatRemainingTime(duration);

                    // BidProductResponseDto 생성
                    return new BidProductResponseDto(bidProduct, remainingTime);
                })
                .sorted(Comparator.comparing(BidProductResponseDto::getExpirationPeriod)) // 마감시간 기준으로 정렬
                .collect(Collectors.toList());

        return bidProductResponses;
    }


    // 특정 경매 상품 조회
    @Transactional
    public BidProductResponseDto getOneBidProduct(Long bidId) {
        BidProduct bidProduct = findBidProductById(bidId);

        // 경매 상품 만료 여부 체크
        if (bidProduct.getExpirationPeriod().isBefore(LocalDateTime.now())) {
            changeToSell(bidProduct.getId());
        }

        // 현재 시간
        LocalDateTime currentTime = LocalDateTime.now();

        // 만료 시간
        LocalDateTime expirationTime = bidProduct.getExpirationPeriod();

        // 남은 시간 계산
        Duration duration = Duration.between(currentTime, expirationTime);

        // 남은 시간을 "X일 Y시간 Z분" 형식으로 포맷팅
        String remainingTime = formatRemainingTime(duration);

        return new BidProductResponseDto(bidProduct, remainingTime);
    }

    // 경매 상품 검색
    @Transactional
    public List<BidProductResponseDto> getSearchedBidProduct(String text) {
        List<BidProduct> bidProductList = bidProductRepository.findAll();

        //경매 상품 만료여부 체크
        checkExpirationPeriod(bidProductList);

        List<BidProductResponseDto> result = new ArrayList<>();
        for (int i = 0; i < bidProductList.size(); i++) {
            if (bidProductList.get(i).getName().contains(text) || bidProductList.get(i).getBrand().getName().contains(text)) {
            // 만약에 "상품의 이름"이나 "상품의 브랜드"가 text를 포함하고 있으면 조회가 되는걸로
                result.add(new BidProductResponseDto(bidProductList.get(i)));
            }
        }
        return result;
    }

//    // 경매 상품 수정
//    @Transactional
//    public BidProductResponseDto updateBidProduct(Long bidId, BidProductRequestDto requestDto, User user) {
//        BidProduct bidProduct = findBidProductById(bidId);
//        if (bidProduct.getUser().equals(user)) {
//            bidProduct.update(requestDto);
//        } else {
//            throw new IllegalArgumentException("본인의 경매상품만 수정할수 있습니다.");
//        }
//        return new BidProductResponseDto(bidProduct);
//    }

    // 경매 상품 삭제
    public void deleteBidProduct(Long bidId, User user) {
        BidProduct bidProduct = findBidProductById(bidId);
        if (bidProduct.getUser().equals(user)) {
            bidProductRepository.delete(bidProduct);
        } else {
            throw new IllegalArgumentException("본인의 경매상품만 삭제할수 있습니다.");
        }
    }

    // 경매 상품 마감
    @Transactional
    public BidProductResponseDto changeToSell(Long bidId) {
        BidProduct bidProduct = findBidProductById(bidId);

        if (bidProduct.getStatus() == 0) {
            bidProduct.changeToSell();
            if (bidProduct.getTopBid() == null) {
                Bid bid = new Bid();
                bid.setBidProduct(bidProduct);
                bid.setUser(bidProduct.getUser());
                bid.setBidPrice(0L);
                bidRepository.save(bid);
                bidProduct.setTopBid(bid);
            }

            //경매 히스토리 테이블 생성
            BidHistory bidHistory = new BidHistory(bidProduct ,bidProduct.getTopBid(), bidProduct.getUser(), bidProduct.getTopBid().getUser());
            bidHistoryRepository.save(bidHistory);
        }

        return new BidProductResponseDto(bidProduct);
    }

    // 경매 상품 유효기한 체크
    @Transactional
    public void checkExpirationPeriod(List<BidProduct> bidProductList) {
        for (int i = 0; i < bidProductList.size(); i++) {
            if (bidProductList.get(i).getExpirationPeriod().isBefore(LocalDateTime.now())) {
                changeToSell(bidProductList.get(i).getId());
            }
        }
        log.info("경매 상품 갱신");
    }

    //------------------ 경매 관련 -----------------------------//

    // 경매 생성(경매 참여)
    @Transactional
    public BidResponseDto createBid(BidRequestDto requestDto, Long bidProductId, User user) {
        BidProduct bidProduct = findBidProductById(bidProductId);

        if (bidProduct.getTopBid() == null) {
            Bid bid = new Bid(requestDto, bidProduct, user);
            bidRepository.save(bid);
            bidProduct.setTopBid(bid);
            return new BidResponseDto(bid);
        }

        if (bidProduct.getTopBid().getBidPrice() >= requestDto.getBidPrice()) {
            throw new IllegalArgumentException("현재 제시한 가격보다 높은 가격이 존재합니다.");
        }

        Bid bid = new Bid(requestDto, bidProduct, user);
        bidRepository.save(bid);
        bidProduct.updateTopBid(bid);
        return new BidResponseDto(bid);
    }

    //---------------------private method-------------------//

    private BidProduct findBidProductById(Long bidId) {
        return bidProductRepository.findById(bidId).orElseThrow(
                () -> new IllegalArgumentException("해당하는 경매 상품을 찾을수 없습니다.")
        );
    }

    // 경매마감까지 남은 시간을 "X일 Y시간 Z분" 형식으로 포맷팅하는 메서드
    private String formatRemainingTime(Duration duration) {
        long days = duration.toDays();
        duration = duration.minusDays(days);
        long hours = duration.toHours();
        duration = duration.minusHours(hours);
        long minutes = duration.toMinutes();

        return String.format(Locale.US, "%d일 %d시간 %d분", days, hours, minutes);
    }

    // 경매 마감 시간 포맷팅
    public String formatExpirationTime(LocalDateTime expirationTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return expirationTime.format(formatter);
    }
}
