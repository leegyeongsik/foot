package com.foot.controller;

import com.foot.dto.bidProduct.BidRequestDto;
import com.foot.dto.bidProduct.BidResponseDto;
import com.foot.entity.User;
import com.foot.security.UserDetailsImpl;
import com.foot.service.BidService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/bid")
public class BidController {

    private BidService bidService;

    public BidController(BidService bidService) {
        this.bidService = bidService;
    }

    // 경매 생성
    @PostMapping("/{bidProductId}")
    public String createBid(@PathVariable Long bidProductId, @ModelAttribute BidRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        bidService.createBid(requestDto, bidProductId, user);
        return "redirect:/view/bp/" + bidProductId;
    }

}
