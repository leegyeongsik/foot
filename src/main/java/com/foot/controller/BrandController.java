package com.foot.controller;

import com.foot.dto.BrandRequestDto;
import com.foot.dto.BrandResponseDto;
import com.foot.service.BrandService;
import lombok.Getter;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brand")
public class BrandController {

    private BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @PostMapping
    public BrandResponseDto createBrand(@RequestBody BrandRequestDto requestDto) {
        return brandService.createBrand(requestDto);
    }

    @GetMapping
    public List<BrandResponseDto> getAllBrand() {
        return brandService.getAllBrand();
    }

    @PutMapping("/{brandId}")
    public BrandResponseDto updateBrand(@PathVariable Long brandId, @RequestBody BrandRequestDto requestDto) {
        return brandService.updateBrand(brandId, requestDto);
    }

    @DeleteMapping("/{brandId}")
    public String deleteBrand(@PathVariable Long brandId) {
        brandService.deleteBrand(brandId);
        return "삭제 완료";
    }

}
