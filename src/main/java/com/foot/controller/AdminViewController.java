package com.foot.controller;

import com.foot.dto.bidProduct.BidProductChartData;
import com.foot.dto.bidProduct.BrandBidProductCount;
import com.foot.dto.bidProduct.BrandResponseDto;
import com.foot.entity.BidProduct;
import com.foot.entity.Product;
import com.foot.entity.User;
import com.foot.entity.UserRoleEnum;
import com.foot.repository.UserRepository;
import com.foot.service.AdminService;
import com.foot.service.BidHistoryService;
import com.foot.service.BrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/view/admin")
@RequiredArgsConstructor
public class AdminViewController {
    private final UserRepository userRepository;
    private final AdminService adminService;
    private final BrandService brandService;
    private final BidHistoryService bidHistoryService;

    // 관리자 홈
    @Secured(UserRoleEnum.Authority.ADMIN)
    @GetMapping("")
    public String adminHomePage(Model model) {
        List<BidProductChartData> chartData = bidHistoryService.getChartData();
        List<BrandBidProductCount> brandBidProductCounts = bidHistoryService.getBrandBidProductCounts();


        // 그래프 데이터를 모델에 추가
        model.addAttribute("chartData", chartData);
        model.addAttribute("brandCounts", brandBidProductCounts);

        return "adminDashboard";
    }

    // 전체 회원 목록 조회
    @Secured(UserRoleEnum.Authority.ADMIN)
    @GetMapping("/users")
    public String getUserList(Model model,
                              @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC)
                              Pageable pageable,
                              String searchKeyword) {

        Page<User> userList = null;

        if (searchKeyword == null) {
            userList = adminService.getUserList(pageable);

        } else {
            userList = adminService.userSearchList(searchKeyword, pageable);
        }

        int nowPage = userList.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 4, userList.getTotalPages());

        model.addAttribute("list", userList);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "adminUserList";
    }

    // 회원 상세 조회
    @Secured(UserRoleEnum.Authority.ADMIN)
    @GetMapping("/user/{id}")
    public String getUser(@PathVariable Long id, Model model) {
        User user = adminService.getUser(id);
        model.addAttribute("user", user);
        return "adminUser";
    }


    // 전체 상품 조회
    @Secured(UserRoleEnum.Authority.ADMIN)
    @GetMapping("/products")
    public String getProductList(Model model,
                                 @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC)
                                 Pageable pageable,
                                 String searchKeyword) {

        Page<Product> productList = null;

        if (searchKeyword == null) {
            productList = adminService.getProductList(pageable);

        } else {
            productList = adminService.productSearchList(searchKeyword, pageable);
        }

        int nowPage = productList.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 4, productList.getTotalPages());

        model.addAttribute("list", productList);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "adminProductList";
    }

    // 전체 경매 상품 조회
    @Secured(UserRoleEnum.Authority.ADMIN)
    @GetMapping("/bidProducts")
    public String getBidProductList(Model model,
                                    @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC)
                                    Pageable pageable,
                                    String searchKeyword) {

        Page<BidProduct> productList = null;

        if (searchKeyword == null) {
            productList = adminService.getBidProductList(pageable);

        } else {
            productList = adminService.bidProductSearchList(searchKeyword, pageable);
        }

        int nowPage = productList.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 4, productList.getTotalPages());

        model.addAttribute("list", productList);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "adminBidProductList";
    }

    // 브랜드 페이지
    @Secured(UserRoleEnum.Authority.ADMIN)
    @GetMapping("/brands")
    public String getBrandList(Model model) {
        List<BrandResponseDto> brandList = brandService.getAllBrand();
        model.addAttribute("brandList", brandList);
        model.addAttribute("size", brandList.size());
        return "adminBrand";
    }


    // 경매 상품 수정 페이지
    @Secured(UserRoleEnum.Authority.ADMIN)
    @GetMapping("/bp/{id}")
    public String getBidProduct(@PathVariable Long id, Model model) {
        BidProduct bidProduct = adminService.getBidProductById(id);
        model.addAttribute("product", bidProduct);
        return "updateBidProduct";
    }


}
