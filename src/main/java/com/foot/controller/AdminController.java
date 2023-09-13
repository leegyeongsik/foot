package com.foot.controller;

import com.foot.dto.AdminUserRequestDto;
import com.foot.dto.bidProduct.BidProductRequestDto;
import com.foot.dto.bidProduct.StatusRequestDto;
import com.foot.dto.products.SaleProductRequestDto;
import com.foot.dto.products.SelectedProductRequestDto;
import com.foot.entity.UserRoleEnum;
import com.foot.repository.UserRepository;
import com.foot.service.AdminService;
import com.foot.service.BidService;
import com.foot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    private final UserService userService;
    private final AdminService adminService;
    private final UserRepository userRepository;
    private final BidService bidService;

    // 회원 정보 수정
    @Secured(UserRoleEnum.Authority.ADMIN)
    @PutMapping("/profile")
    public ResponseEntity<String> updateUser(@RequestBody AdminUserRequestDto requestDto) {
        adminService.updateUser(requestDto);
        return ResponseEntity.ok().body("변경 성공");
    }


    // 회원 강제 탈퇴
    @Secured(UserRoleEnum.Authority.ADMIN)
    @DeleteMapping("/profile/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            adminService.deleteUser(id);
            return ResponseEntity.ok("회원 탈퇴 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원 탈퇴 실패");
        }
    }


    // 상품 할인율 변경
    @Secured(UserRoleEnum.Authority.ADMIN)
    @PostMapping("/products/updateDiscountRates")
    public ResponseEntity<String> updateDiscountRates(@RequestBody SaleProductRequestDto requestDto) {
        adminService.updateProductDiscountRates(requestDto);
        return ResponseEntity.ok("Discount rates updated successfully");
    }


    // 상품 삭제
    @Secured(UserRoleEnum.Authority.ADMIN)
    @DeleteMapping("/products")
    public ResponseEntity<String> deleteProducts(@RequestBody SelectedProductRequestDto requestDto) {
        adminService.deleteProducts(requestDto);
        return ResponseEntity.ok("Products deleted successfully");
    }

    // 경매 상품 삭제
    @Secured(UserRoleEnum.Authority.ADMIN)
    @DeleteMapping("/bidProducts")
    public ResponseEntity<String> deleteBidProducts(@RequestBody SelectedProductRequestDto requestDto) {
        adminService.deleteBidProducts(requestDto);
        return ResponseEntity.ok("BidProducts deleted successfully");
    }

    // 경매 상품 수정
    @Secured(UserRoleEnum.Authority.ADMIN)
    @PostMapping("/bidProducts/{id}")
    public String updateBidProducts(@ModelAttribute BidProductRequestDto requestDto, @PathVariable Long id) throws IOException {
        bidService.updateBidProduct(requestDto, id);
        return "redirect:/view/admin";
    }


    // 경매 상품 상태 변경
    @Secured(UserRoleEnum.Authority.ADMIN)
    @PostMapping("/bidProducts/updateStatus")
    public ResponseEntity<String> updateStatus(@RequestBody StatusRequestDto requestDto) {
        bidService.updateStatus(requestDto);
        return ResponseEntity.ok("Status updated successfully");
    }

}
