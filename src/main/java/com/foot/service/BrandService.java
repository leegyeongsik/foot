package com.foot.service;

import com.foot.dto.bidProduct.BrandRequestDto;
import com.foot.dto.bidProduct.BrandResponseDto;
import com.foot.entity.Brand;
import com.foot.repository.BrandRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BrandService {

    private BrandRepository brandRepository;

    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public BrandResponseDto createBrand(BrandRequestDto requestDto) {
        Brand brand = new Brand(requestDto.getName());
        brandRepository.save(brand);
        return new BrandResponseDto(brand);
    }

    public List<BrandResponseDto> getAllBrand() {

        List<Brand> brandList = brandRepository.findAll();
        List<BrandResponseDto> brandResponseDtoList = new ArrayList<>();
        for (int i = 0; i < brandList.size(); i++) {
            brandResponseDtoList.add(new BrandResponseDto(brandList.get(i)));
        }

        return brandResponseDtoList;
    }

    @Transactional
    public BrandResponseDto updateBrand(Long brandId, BrandRequestDto requestDto) {
        Brand brand = findById(brandId);
        brand.update(requestDto);
        return new BrandResponseDto(brand);
    }

    public void deleteBrand(Long brandId) {
        Brand brand = findById(brandId);
        brandRepository.delete(brand);
    }



    //-----------------------private method--------------------//

    private Brand findById(Long brandId) {
        return brandRepository.findById(brandId).orElseThrow(
                () -> new IllegalArgumentException("선택한 메모를 찾을수 없습니다.")
        );
    }
}
