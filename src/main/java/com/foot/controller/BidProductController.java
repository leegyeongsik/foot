package com.foot.controller;

import com.foot.dto.bidProduct.BidProductRequestDto;
import com.foot.dto.bidProduct.BidProductResponseDto;
import com.foot.entity.User;
import com.foot.security.UserDetailsImpl;
import com.foot.service.BidService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
    public BidProductResponseDto createBidProduct(@ModelAttribute BidProductRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        User user = userDetails.getUser();
        return bidService.createBidProduct(requestDto, user);
    }

    // 경매 상품 생성 (이미지 파일 저장)
    @PostMapping("/save")
    public String save(@ModelAttribute BidProductRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        System.out.println(requestDto);
        bidService.save(requestDto, userDetails.getUser());
        return "index";
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
//    @PutMapping("/{bidProductId}")
//    public BidProductResponseDto updateBidProduct(@PathVariable Long bidProductId, @RequestBody BidProductRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        User user = userDetails.getUser();
//        return bidService.updateBidProduct(bidProductId, requestDto, user);
//    }

    // 경매 상품 삭제
    @DeleteMapping("/{bidProductId}")
    public String deleteBidProduct(@PathVariable Long bidProductId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        bidService.deleteBidProduct(bidProductId, user);
        return "삭제 완료";
    }

    // 경매 상품 마감
    @PutMapping("/sell/{bidProductId}")
    public BidProductResponseDto changeToSell(@PathVariable Long bidProductId) {
        return bidService.changeToSell(bidProductId);
    }
}