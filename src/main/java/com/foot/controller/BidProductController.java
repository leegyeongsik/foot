package com.foot.controller;

import com.foot.dto.BidProductRequestDto;
import com.foot.dto.BidProductResponseDto;
import com.foot.service.BidService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bidProduct")
public class BidProductController {

    private BidService bidService;

    public BidProductController(BidService bidService) {
        this.bidService = bidService;
    }

    // 경매 상품 생성
    @PostMapping
    public BidProductResponseDto createBidProduct(@RequestBody BidProductRequestDto requestDto) {
        return bidService.createBidProduct(requestDto);
    }

    // 경매 상품 전체 조회
    @GetMapping
    public List<BidProductResponseDto> getAllBidProduct() {
        return bidService.getAllBidProduct();
    }

    // 특정 경매 상품 조회
    @GetMapping("/{bidProductId}")
    public BidProductResponseDto getOneBidProduct(@PathVariable Long bidProductId) {
        return bidService.getOneBidProduct(bidProductId);
    }

    // text로 경매 상품 검색(queryParam형태로 받는것을 생각중 /api/bid?text=~~ )
    @GetMapping("/search")
    public List<BidProductResponseDto> getSearchedBidProduct(@RequestParam String text) {
        return bidService.getSearchedBidProduct(text);
    }

    // 경매 상품 수정
    @PutMapping("/{bidProductId}")
    public BidProductResponseDto updateBidProduct(@PathVariable Long bidProductId, @RequestBody BidProductRequestDto requestDto) {
        return bidService.updateBidProduct(bidProductId, requestDto);
    }

    // 경매 상품 삭제
    @DeleteMapping("/{bidProductId}")
    public String deleteBidProduct(@PathVariable Long bidProductId) {
        bidService.deleteBidProduct(bidProductId);
        return "삭제 완료";
    }

    // 경매 상품 마감
    @PutMapping("/sell/{bidProductId}")
    public BidProductResponseDto changeToSell(@PathVariable Long bidProductId) {
        return bidService.changeToSell(bidProductId);
    }
}
