package com.foot.service;

import com.foot.dto.BidProductRequestDto;
import com.foot.dto.BidProductResponseDto;
import com.foot.dto.BidRequestDto;
import com.foot.dto.BidResponseDto;
import com.foot.entity.Bid;
import com.foot.entity.BidProduct;
import com.foot.entity.Brand;
import com.foot.repository.BidProductRepository;
import com.foot.repository.BidRepository;
import com.foot.repository.BrandRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BidService {

    private BidProductRepository bidProductRepository;
    private BidRepository bidRepository;
    private BrandRepository brandRepository;

    public BidService(BidProductRepository bidProductRepository, BidRepository bidRepository, BrandRepository brandRepository) {
        this.bidProductRepository = bidProductRepository;
        this.bidRepository = bidRepository;
        this.brandRepository = brandRepository;
    }

    // 경매 상품 생성
    public BidProductResponseDto createBidProduct(BidProductRequestDto requestDto) {
        Brand brand = brandRepository.findByName(requestDto.getBrand());
        BidProduct bidProduct = new BidProduct(requestDto, brand);
        bidProductRepository.save(bidProduct);
        return new BidProductResponseDto(bidProduct);
    }

    // 경매 상품 전체 조회
    public List<BidProductResponseDto> getAllBidProduct() {
        List<BidProduct> bidProductList = bidProductRepository.findAll();
        List<BidProductResponseDto> bidProductResponseDtoList = new ArrayList<>();
        for (int i = 0; i < bidProductList.size(); i++) {
            bidProductResponseDtoList.add(new BidProductResponseDto(bidProductList.get(i)));
        }
        return bidProductResponseDtoList;
    }

    // 특정 경매 상품 조회
    public BidProductResponseDto getOneBidProduct(Long bidId) {
        BidProduct bidProduct = findBidProductById(bidId);
        return new BidProductResponseDto(bidProduct);
    }

    // 경매 상품 검색
    public List<BidProductResponseDto> getSearchedBidProduct(String text) {
        List<BidProduct> bidProductList = bidProductRepository.findAll();
        List<BidProductResponseDto> result = new ArrayList<>();
        for (int i = 0; i < bidProductList.size(); i++) {
            if (bidProductList.get(i).getName().contains(text) || bidProductList.get(i).getBrand().getName().contains(text)) {
            // 만약에 "상품의 이름"이나 "상품의 브랜드"가 text를 포함하고 있으면 조회가 되는걸로
                result.add(new BidProductResponseDto(bidProductList.get(i)));
            }
        }
        return result;
    }

    // 경매 상품 수정
    @Transactional
    public BidProductResponseDto updateBidProduct(Long bidId, BidProductRequestDto requestDto) {
        BidProduct bidProduct = findBidProductById(bidId);
        bidProduct.update(requestDto);
        return new BidProductResponseDto(bidProduct);
    }

    // 경매 상품 삭제
    public void deleteBidProduct(Long bidId) {
        BidProduct bidProduct = findBidProductById(bidId);
        bidProductRepository.delete(bidProduct);
    }

    // 경매 상품 마감
    public BidProductResponseDto changeToSell(Long bidId) {
        BidProduct bidProduct = findBidProductById(bidId);
        bidProduct.changeToSell();
        return new BidProductResponseDto(bidProduct);
    }

    // 경매 생성(경매 참여)
    public BidResponseDto createBid(BidRequestDto requestDto, Long bidProductId) {
        BidProduct bidProduct = findBidProductById(bidProductId);
        Bid bid = new Bid(requestDto, bidProduct);
        bidRepository.save(bid);
        return new BidResponseDto(bid);
    }





    //---------------------private method-------------------//

    private BidProduct findBidProductById(Long bidId) {
        return bidProductRepository.findById(bidId).orElseThrow(
                () -> new IllegalArgumentException("해당하는 경매 상품을 찾을수 없습니다.")
        );
    }
}
