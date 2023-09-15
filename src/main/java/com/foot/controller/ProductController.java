package com.foot.controller;

import com.foot.dto.ApiResponseDto;
import com.foot.dto.OrderRequestDto;
import com.foot.dto.products.*;
import com.foot.exception.OutOfProductException;
import com.foot.kafka.OrderProducer;
import com.foot.repository.products.ProductColorRepository;
import com.foot.security.UserDetailsImpl;
import com.foot.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
@Slf4j

public class ProductController {
    private final OrderProducer orderProducer;
    private final ProductColorRepository repository;
    private static String BOOT_TOPIC = "kafka-order";

    private final ProductService productService;

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    // 상품생성
    public void createProduct(@ModelAttribute ProductRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException { //
        productService.createProduct(requestDto, userDetails.getUser());
    }

    @GetMapping("") // 전체 상품 조회
    public ProductsResponseDto getProduct(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            return productService.getProduct();
        } else {
            return productService.getUserProduct(userDetails.getUser());
        }
    }

    @GetMapping("/{productId}") // 특정 상품 조회
    public innerProductResponseDto getTargetProduct(@PathVariable Long productId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            return productService.getTargetProduct(productId);
        } else {
            return productService.getTargetUserProduct(productId, userDetails.getUser());
        }
    }

    @PutMapping("/{productId}") // 특정 상품 수정
    public void updateProduct(@PathVariable Long productId, @ModelAttribute UpdateProductResponseDto updateProductResponseDto) {
        productService.updateProduct(productId, updateProductResponseDto);
    }

    @DeleteMapping("/{productId}") // 특정 상품 삭제
    public void deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
    }

    @GetMapping("/") // 카테고리 상품 조회
    public List<ProductResponseDto> getCategoryProduct(@RequestParam("brand") String brand, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if(userDetails == null) {
            return productService.getCategoryProduct(brand);
        } else {
            return productService.getCategoryUserProduct(brand, userDetails.getUser());
        }
    }


    @GetMapping("/search") // 검색하고 검색한거에 맞는 사람 쭉 나오는데 해당 보드에 이미 초대된 사람이라면 true로 넣어줌 아니면 false
    public List<ProductResponseDto> getSearch(@RequestParam("name")String name){
        return productService.getSearch(name);
    }

    @GetMapping("size/{productId}") // 수정 -> 사이즈에 맞는 컬러 반환
    public innerProductResponseDto getSizeProduct (@PathVariable Long productId) {
        return productService.getSizeProduct(productId);

    }
    @PostMapping("/order") // 오더
    public void OrderProduct (@RequestBody OrderRequestDto orderRequestDto , @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if( repository.findById(orderRequestDto.getOrderProductcolorId()).get().getAmount() - orderRequestDto.getOrderCount() < 0){
            throw new OutOfProductException(String.valueOf(orderRequestDto.getOrderCartItemId()));
            // 해당 상품 사이즈의 컬러 재고가 주문된 개수보다 많다면 exception
        } else {
            orderRequestDto.setOrderUserId(userDetails.getUser().getId());
            orderProducer.send(BOOT_TOPIC, orderRequestDto);
        }
    }
    @ExceptionHandler({OutOfProductException.class})
    public ResponseEntity<ApiResponseDto> handleMethodArgumentNotValidException(OutOfProductException ex){
        ApiResponseDto apiResponseDto = new ApiResponseDto(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(
            apiResponseDto,
            HttpStatus.BAD_REQUEST
        );
    }

}
