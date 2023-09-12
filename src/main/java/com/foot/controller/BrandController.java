package com.foot.controller;

import com.foot.dto.bidProduct.BrandRequestDto;
import com.foot.dto.bidProduct.BrandResponseDto;
import com.foot.service.BrandService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brand")
public class BrandController {

    private BrandService brandService;
    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    // 브랜드 생성
    @PostMapping
    public BrandResponseDto createBrand(@RequestBody BrandRequestDto requestDto) {
        return brandService.createBrand(requestDto);
    }

    // 브랜드 조회
    @GetMapping
    public List<BrandResponseDto> getAllBrand() {
        return brandService.getAllBrand();
    }

    // 브랜드 수정
    @PutMapping("/{brandId}")
    public ResponseEntity<String> updateBrand(@PathVariable Long brandId, @RequestBody BrandRequestDto requestDto) {
        brandService.updateBrand(brandId, requestDto);
        return ResponseEntity.ok("브랜드 수정 완료");
    }

    // 브랜드 삭제
    @DeleteMapping("/{brandId}")
    public String deleteBrand(@PathVariable Long brandId) {
        brandService.deleteBrand(brandId);
        return "삭제 완료";
    }

}
