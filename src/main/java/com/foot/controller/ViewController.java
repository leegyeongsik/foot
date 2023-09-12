package com.foot.controller;

import com.foot.dto.bidProduct.BidProductResponseDto;
import com.foot.dto.bidProduct.BrandResponseDto;
import com.foot.entity.Bid;
import com.foot.entity.Brand;
import com.foot.service.BidService;
import com.foot.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ViewController {
    private final BidService bidService;
    private final BrandService brandService;

    @GetMapping("/bp/resist")
    public String getBpResister() {
        return "bpResist";
    }

    @GetMapping("/view/bp/create")
    public String bpSavePage() {
        return "createBidProduct";
    }

    @GetMapping("/view/bp/{bpId}")
    public String bidProductPage(@PathVariable Long bpId, Model model) {
        BidProductResponseDto bidProduct = bidService.getOneBidProduct(bpId);
        model.addAttribute("product", bidProduct);
        return "innerBidProduct";
    }


    @GetMapping("/view/bp")
    public String getActiveBidProducts(Model model) {
        List<BidProductResponseDto> activeBidProducts = bidService.getActiveBidProducts();
        List<BrandResponseDto> brands = brandService.getAllBrand();

        model.addAttribute("products", activeBidProducts);
        model.addAttribute("brands", brands);

        return "bidProductList";
    }

    @GetMapping("/view/bp/brand/{brandId}")
    public String getBidProductsByBrand(Model model, @PathVariable Long brandId) {
        List<BidProductResponseDto> activeBidProducts = bidService.getBidProductsByBrand(brandId);
        List<BrandResponseDto> brands = brandService.getAllBrand();

        model.addAttribute("products", activeBidProducts);
        model.addAttribute("brands", brands);

        return "bidProductList";
    }


}
