package com.foot.controller;

import com.foot.dto.bidProduct.BidProductRequestDto;
import com.foot.dto.bidProduct.BidProductResponseDto;
import com.foot.entity.User;
import com.foot.security.UserDetailsImpl;
import com.foot.service.BidService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/api/bidProduct")
public class BidProductController {

    private BidService bidService;

    public BidProductController(BidService bidService) {
        this.bidService = bidService;
    }

    // 경매 상품 생성
    @PostMapping
    public String createBidProduct(@ModelAttribute BidProductRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        User user = userDetails.getUser();
        bidService.createBidProduct(requestDto, user);
        return "redirect:/";
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


    // 경매 상품 삭제
    @DeleteMapping("/{bidProductId}")
    public void deleteBidProduct(@PathVariable Long bidProductId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        bidService.deleteBidProduct(bidProductId, user);
    }

    // 경매 상품 마감
    @PutMapping("/sell/{bidProductId}")
    public BidProductResponseDto changeToSell(@PathVariable Long bidProductId) {
        return bidService.changeToSell(bidProductId);
    }
}
