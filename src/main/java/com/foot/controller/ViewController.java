package com.foot.controller;

import com.foot.dto.bidProduct.BidProductResponseDto;
import com.foot.entity.BidProduct;
import com.foot.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
public class ViewController {
    private final BidService bidService;

    @GetMapping("/bp/resist")
    public String getBpResister() {
        return "bpResist";
    }

    @GetMapping("/view/bpSave")
    public String bpSavePage() {
        return "createBidProduct";
    }

    @GetMapping("/view/bp/{bpId}")
    public String bidProductPage(@PathVariable Long bpId, Model model) {
        BidProductResponseDto bidProduct = bidService.getOneBidProduct(bpId);
        model.addAttribute("product", bidProduct);
        return "innerBidProduct";
    }


}
