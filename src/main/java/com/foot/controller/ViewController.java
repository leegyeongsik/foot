package com.foot.controller;

import com.foot.dto.bidProduct.BidProductResponseDto;
import com.foot.dto.bidProduct.BrandResponseDto;
import com.foot.entity.Bid;
import com.foot.entity.BidProduct;
import com.foot.entity.Brand;
import com.foot.security.UserDetailsImpl;
import com.foot.service.BidService;
import com.foot.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

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

    // 본인이 등록한 경매 상품 확인하는 페이지
    @GetMapping("/view/myBp")
    public String getMyBidProduct(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<BidProductResponseDto> products = bidService.getUserBidProducts(userDetails.getUser());

        model.addAttribute("products", products);
        return "myBidProducts";
    }

    // 본인이 입찰한 경매 상품 확인하는
    @GetMapping("/view/myBids")
    public String getUserBids(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 유저가 입찰한 BidProduct와 최고 입찰 가격을 가져옴
        Map<Long, Long> prices = bidService.getHighestBidPricesByUser(userDetails.getUser());
        List<BidProductResponseDto> products = bidService.getBidProductByBidUser(userDetails.getUser());

        // 모델에 데이터 추가
        model.addAttribute("userBidProducts", products);
        model.addAttribute("highestBidPrices", prices);

        return "myBids"; // 뷰 이름 반환
    }


}
