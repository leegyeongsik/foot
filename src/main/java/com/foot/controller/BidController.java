package com.foot.controller;

import com.foot.dto.bidProduct.BidRequestDto;
import com.foot.dto.bidProduct.BidResponseDto;
import com.foot.entity.User;
import com.foot.security.UserDetailsImpl;
import com.foot.service.BidService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public BidResponseDto createBid(@PathVariable Long bidProductId, @RequestBody BidRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        return bidService.createBid(requestDto, bidProductId, user);
    }

}
