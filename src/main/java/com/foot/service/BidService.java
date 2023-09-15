package com.foot.service;

import com.foot.dto.bidProduct.*;
import com.foot.entity.*;
import com.foot.repository.BidHistoryRepository;
import com.foot.repository.BidProductRepository;
import com.foot.repository.BidRepository;
import com.foot.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
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

    // 브랜드 별 경매 상품 조회
    @Transactional
    public List<BidProductResponseDto> getBidProductsByBrand(Long brandId) {
        Optional<Brand> brand = brandRepository.findById(brandId);

        if (brand.isPresent()) {
            // Brand가 존재하는 경우
            Brand targetBrand = brand.get();

            List<BidProduct> activeBidProducts = bidProductRepository.findByStatusAndBrand(0, targetBrand); // 상태가 0이고 특정 Brand에 속한 활성 경매 상품 조회
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
        } else {
            // Brand가 존재하지 않는 경우에 대한 처리를 여기에 추가할 수 있습니다.
            // 예를 들어 예외를 던지거나 빈 리스트를 반환하거나 다른 작업을 수행할 수 있습니다.
            return Collections.emptyList(); // Brand가 없는 경우 빈 리스트 반환
        }
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

    // 유저가 등록한 경매상품 조회
    public List<BidProductResponseDto> getUserBidProducts(User user) {
        List<BidProduct> bidProductList = bidProductRepository.findByUser(user);

        checkExpirationPeriod(bidProductList);

        // 현재 시간
        LocalDateTime currentTime = LocalDateTime.now();

        List<BidProductResponseDto> bidProductResponses = bidProductList.stream()
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

    // 유저가 입찰한 경매상품 조회
    public List<BidProductResponseDto> getBidProductByBidUser(User user) {
        List<BidProduct> bidProductList = bidProductRepository.findByBidsUser(user);

        // 현재 시간
        LocalDateTime currentTime = LocalDateTime.now();

        List<BidProductResponseDto> bidProductResponses = bidProductList.stream()
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

    // 상품별 자신의 최고 입찰 가격 반환(상품의 topBid가 아닌 본인이 입찰한 가격중 가장 높은 가격)
    public Map<Long, Long> getHighestBidPricesByUser(User user) {
        List<BidProduct> userBidProducts = bidProductRepository.findByBidsUser(user);

        // 각 BidProduct에 대한 유저의 최고 입찰 가격을 계산하여 Map으로 반환
        return userBidProducts.stream()
                .collect(Collectors.toMap(
                        BidProduct::getId,  // BidProduct의 ID를 키로 사용
                        bidProduct -> bidProduct.getBids().stream()
                                .filter(bid -> bid.getUser().getId() == user.getId()) // 해당 유저의 입찰만 필터링
                                .mapToLong(Bid::getBidPrice)  // 입찰 리스트에서 입찰 가격만 추출
                                .max()  // 최대값 계산
                                .orElse(0L)  // 입찰 내역이 없을 경우 0을 반환
                ));
    }


    // 경매 상품 상태 변경
    public void updateStatus(StatusRequestDto requestDto) {
        List<Long> productIds = requestDto.getProductIds();
        int status = requestDto.getStatus();
        LocalDateTime endDate = (status == 1)
                ? LocalDateTime.now().minusSeconds(1).withNano(0)
                : LocalDateTime.now().plusDays(1).withNano(0);

        for (Long productId : productIds) {
            BidProduct product = findBidProductById(productId);
            if (product != null) {
                // 경매 상태 업데이트
                product.setStatus(status);
                product.setExpirationPeriod(endDate);

                // 상품 저장
                bidProductRepository.save(product);

                if (status == 1 && product.getTopBid() != null) { // topBid가 null이 아닌 경우에만 BidHistory 생성
                    // 경매 히스토리 테이블 생성
                    BidHistory bidHistory = new BidHistory(product, product.getTopBid(), product.getUser());
                    bidHistoryRepository.save(bidHistory);
                }
            }
        }
    }


    // 경매 상품 삭제
    public void deleteBidProduct(Long bidId, User user) {
        BidProduct bidProduct = findBidProductById(bidId);
        if (bidProduct.getUser().getId() == user.getId()) {
            bidProductRepository.delete(bidProduct);
        } else {
            throw new IllegalArgumentException("본인의 경매상품만 삭제할수 있습니다.");
        }
    }

    // 경매 상품 수정
    public void updateBidProduct(BidProductRequestDto requestDto, Long id) throws IOException {
        BidProduct bidProduct = findBidProductById(id);
        Brand brand = brandRepository.findByName(requestDto.getBrand());

        // 아무 이미지도 첨부하지 않았을 경우 수정 x
        if (!requestDto.getBidProductFile().isEmpty()) {
            bidProduct.setFootpicture(s3UploadService.uploadImage(requestDto.getBidProductFile()));
        }

        bidProduct.setName(requestDto.getName());
        bidProduct.setBrand(brand);
        bidProduct.setDescription(requestDto.getDescription());
        bidProduct.setFeetsize(requestDto.getFeetSize());
        bidProduct.setFootsize(requestDto.getFootSize());
        bidProduct.setStartPrice(requestDto.getStartPrice());
        bidProduct.setExpirationPeriod(requestDto.getExpirationPeriod());

        bidProductRepository.save(bidProduct);
    }


    // 경매 상품 마감
    @Transactional
    public BidProductResponseDto changeToSell(Long bidId) {
        BidProduct bidProduct = findBidProductById(bidId);

        if (bidProduct.getStatus() == 0) {
            bidProduct.changeToSell();

            Bid topBid = bidProduct.getTopBid();

            if (topBid != null) { // topBid가 null이 아닌 경우에만 BidHistory 생성
                // 경매 히스토리 테이블 생성
                BidHistory bidHistory = new BidHistory(bidProduct, topBid, bidProduct.getUser());
                bidHistoryRepository.save(bidHistory);
            }
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

        // Status가 1인 경우 경매를 못하도록 예외 처리
        if (bidProduct.getStatus() == 1) {
            throw new IllegalArgumentException("이미 종료된 경매 상품입니다.");
        }

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

}
