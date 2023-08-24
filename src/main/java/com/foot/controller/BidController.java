package com.foot.controller;

import com.foot.dto.BidRequestDto;
import com.foot.dto.BidResponseDto;
import com.foot.service.BidService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bid")
public class BidController {

    private BidService bidService;

    public BidController(BidService bidService) {
        this.bidService = bidService;
    }

    // 경매 생성
    @PostMapping("/{bidProductId}")
    public BidResponseDto createBid(@PathVariable Long bidProductId, @RequestBody BidRequestDto requestDto) {
        return bidService.createBid(requestDto, bidProductId);
    }

}
