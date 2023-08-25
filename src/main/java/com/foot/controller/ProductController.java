package com.foot.controller;

import com.foot.dto.products.*;
import com.foot.security.UserDetailsImpl;
import com.foot.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;
    @PostMapping(value = "",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE) // 상품생성
    public void createProduct(@ModelAttribute ProductRequestDto requestDto , @AuthenticationPrincipal UserDetailsImpl userDetails) {
        productService.createProduct(requestDto , userDetails.getUser());
    }

    @GetMapping("") // 전체 상품 조회
    public List<ProductResponseDto> getProduct() {
        return productService.getProduct();
    }

    @GetMapping("/{productId}") // 특정 상품 조회
    public innerProductResponseDto getTargetProduct(@PathVariable Long productId) {
        return productService.getTargetProduct(productId);
    }

    @PutMapping("/{productId}") // 특정 상품 수정
    public void updateProduct(@PathVariable Long productId , @ModelAttribute UpdateProductResponseDto updateProductResponseDto
    , @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        System.out.println(updateProductResponseDto.getName());
//        System.out.println(updateProductResponseDto.getDescription());
//        System.out.println(updateProductResponseDto.getPrice());
//        System.out.println(updateProductResponseDto.getTotalAmount());
//        System.out.println(updateProductResponseDto.getProductSizeInfo());
//        System.out.println(updateProductResponseDto.getProductColorAmount());
        productService.updateProduct(productId,updateProductResponseDto , userDetails.getUser());
    }

    @DeleteMapping("/{productId}") // 특정 상품 삭제
    public void deleteProduct(@PathVariable Long productId , @AuthenticationPrincipal UserDetailsImpl userDetails) {
        productService.deleteProduct(productId , userDetails.getUser());
    }

//    @GetMapping("/") // 카테고리 상품 조회
//    public void getCategoryProduct (@RequestParam("brand")String brand) {
//        productService.getCategoryProduct(brand);
//    }

    @PostMapping("/{productId}") // 특정 상품 세일 추가
    public void updateSaleProduct (@PathVariable Long productId , @RequestBody SaleProductRequestDto requestDto
            , @AuthenticationPrincipal UserDetailsImpl userDetails) {
        productService.updateSaleProduct(productId,requestDto , userDetails.getUser());
    }

    @PostMapping("foot/{footId}") // 상품 신발정보수정
    public void updateFootProduct (@RequestBody FootProductRequestDto requestDto ,@PathVariable Long footId
            , @AuthenticationPrincipal UserDetailsImpl userDetails) {
        productService.updateFootProduct(requestDto,footId , userDetails.getUser());
    }


    @GetMapping("/search") // 검색하고 검색한거에 맞는 사람 쭉 나오는데 해당 보드에 이미 초대된 사람이라면 true로 넣어줌 아니면 false
    public List<ProductResponseDto> getSearch(@RequestParam("name")String name){
        return productService.getSearch(name);
    }

    @GetMapping("size/{productId}") // 수정 -> 사이즈에 맞는 컬러 반환
    public innerProductResponseDto getSizeProduct (@PathVariable Long productId) {
        return productService.getSizeProduct(productId);

    }

}
