package com.foot.service;

import com.foot.dto.products.*;
import com.foot.entity.*;
import com.foot.repository.products.ProductColorImgRepository;
import com.foot.repository.products.ProductColorRepository;
import com.foot.repository.products.ProductRepository;
import com.foot.repository.products.ProductSizeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor

public class ProductService {
    private final ProductRepository productRepository;
    private final ProductColorImgRepository productColorImgRepository;
    private final ProductColorRepository productColorRepository;
    private final ProductSizeRepository productSizeRepository;
    private final S3UploadService s3UploadService;

    @Transactional
    public void createProduct(ProductRequestDto requestDto , User user) throws IOException { // 상품등록
//        confirmAdminToken(user);
        List<MultipartFile> modelColorImg = requestDto.getModelColorImg();
        List<String> modelColorName = requestDto.getModelColorName();

        List<Long> modelAmount = requestDto.getModelAmount();
        List<Long> modelSize= requestDto.getModelSize();

        List<Long> modelFootSize = requestDto.getModelFootSize();
        List<Long> modelFeetSize=requestDto.getModelFeetSize();


        HashMap<Integer, ArrayList<String>> modelColor = requestDto.getModelColor();
        HashMap<Integer, ArrayList<Long>> modelColorAmount = requestDto.getModelColorAmount();


        Product product = Product.builder() // 모델 생성
                .model(requestDto.getName())
                .price(requestDto.getPrice())
                .TotalAmount(requestDto.getTotalAmount())
                .Description(requestDto.getDescription())
                .modelpicture(s3UploadService.uploadImage(requestDto.getModelPicture())) // 들어온거 업로드해서 주소 넣어줌
                .user(user)
                .build();

        productRepository.save(product);


        for (int i = 0; i < modelColorImg.size(); i++) { // 모델마다의 컬러 생성
            ProductColorImg productColorImg = ProductColorImg.builder()
                    .colorname(modelColorName.get(i))
                    .colorimg(s3UploadService.uploadImage(modelColorImg.get(i)))
                    .product(product).build();
            productColorImgRepository.save(productColorImg);
        }

        for (int i = 0; i < modelSize.size(); i++) { // 모델 사이즈 생성
            ProductSize productSize = ProductSize.builder()
                    .size(modelSize.get(i))
                    .amount(modelAmount.get(i))
                    .product(product)
                    .footsize(modelFootSize.get(i))
                    .feetsize(modelFeetSize.get(i))
                    .footpicture(product.getModelpicture())
                    .build();

            productSizeRepository.save(productSize);
            List<String> modelColors = modelColor.get(i); // 모델 컬러의 남은 수량
            List<Long> modelColorAmounts = modelColorAmount.get(i);
            for (int j = 0; j < modelColors.size(); j++) {
                ProductColorImg productColorImg = productRepository.getModelColor(modelColors.get(j), product.getId());
                ProductColor productColor = ProductColor.builder()
                        .productColorImg(productColorImg)
                        .productSize(productSize)
                        .amount(modelColorAmounts.get(j))
                        .build();
                productColorRepository.save(productColor);
            }
        }
    }

    public List<ProductResponseDto> getProduct() { // 전체 상품 조회
        List<ProductResponseDto> productResponseDtos = new ArrayList<>(); // cursor 페이지네이션
        List<Product> products=productRepository.findAll();
        for (Product product : products) {
            productResponseDtos.add(new ProductResponseDto(product));
        }
        return productResponseDtos;
    }

    public innerProductResponseDto getTargetProduct(Long productId) { // 특정 상품 조회
        List<ProductColorResponseDto> productColorResponseDtos = new ArrayList<>();
        Product product=productRepository.findById(productId).get();
        List<ProductColorImg> productColorImgs=productRepository.getModelColors(productId);
        for (ProductColorImg colorImg : productColorImgs) {
            List<ProductSizeResponseDto> productSizeResponseDtos = new ArrayList<>();
            Map<ProductSize, ProductColor> sizeProductColorMap =
                    productRepository.getModelSizeOfColor(productId , colorImg.getId());
            for (ProductSize productSize : sizeProductColorMap.keySet()) {
                productSizeResponseDtos.add(new ProductSizeResponseDto(productSize ,sizeProductColorMap.get(productSize)));
            }
            productColorResponseDtos.add(new ProductColorResponseDto(colorImg,productSizeResponseDtos));
        }
        return new innerProductResponseDto(new ProductResponseDto(product),productColorResponseDtos);
    }

    public innerProductResponseDto getSizeProduct(Long productId) { // 특정 상품 조회
        Product product=productRepository.findById(productId).get();
        List<ProductSizeResponseDto> productSizeResponseDtos = new ArrayList<>();

        List<ProductSize> productSizes = productRepository.getModelSize(productId);
        for (ProductSize productSize : productSizes) {
            List<ProductColorResponseDto> productColorResponseDtos = new ArrayList<>();
            Map<ProductColor, ProductColorImg> sizeProductColor =
                    productRepository.getSizeColorProduct(productId , productSize.getId());

            for (ProductColor productColor : sizeProductColor.keySet()) {
                productColorResponseDtos.add(new ProductColorResponseDto(productColor ,sizeProductColor.get(productColor) ));
            }
            productSizeResponseDtos.add(new ProductSizeResponseDto(productSize , productColorResponseDtos));

        }
        return new innerProductResponseDto(new ProductResponseDto(product) , productSizeResponseDtos ,   "null");
    }


    public void updateProduct(Long productId, UpdateProductResponseDto updateProductResponseDto) {
//        confirmAdminToken(user);

        Product product=productRepository.findById(productId).get();
        product.updateProduct(updateProductResponseDto.getName() , updateProductResponseDto.getDescription(),
                updateProductResponseDto.getPrice() , updateProductResponseDto.getTotalAmount());
        productRepository.save(product);

        HashMap<Integer, ArrayList<Long>> modelColorAmount = updateProductResponseDto.getProductColorAmount();
        for (Integer integer : modelColorAmount.keySet()) {
            List<Long> ColorAmount=modelColorAmount.get(integer);
            ProductColor productColor=productColorRepository.findById(ColorAmount.get(0)).get();
            productColor.updateProductColor(ColorAmount.get(1));

            productColorRepository.save(productColor);
        }
        HashMap<Integer, ArrayList<Long>> modelProductSizeInfo = updateProductResponseDto.getProductSizeInfo();
        for (Integer integer : modelProductSizeInfo.keySet()) {
            List<Long>  SizeInfo = modelProductSizeInfo.get(integer);
            ProductSize productSize=productSizeRepository.findById(SizeInfo.get(0)).get();
            productSize.updateProductSize(SizeInfo.get(1) , SizeInfo.get(2) , SizeInfo.get(3) , SizeInfo.get(4));
            productSizeRepository.save(productSize);

        }
        

    }

    public void deleteProduct(Long productId) {
//        confirmAdminToken(user);

        Product product=productRepository.findById(productId).get();
        s3UploadService.deleteFile(product.getModelpicture());
        List<ProductColorImg> productColorImgs = productRepository.getModelColors(productId);
        for (ProductColorImg colorImg : productColorImgs) {
            s3UploadService.deleteFile(colorImg.getColorimg());
        }
        productRepository.delete(product);
    }

//    public void updateSaleProduct(Long productId, SaleProductRequestDto requestDto, User user) {
////        confirmAdminToken(user);
//
//        Product product=productRepository.findById(productId).get();
//        product.addSale(requestDto);
//    }

    public void updateFootProduct(FootProductRequestDto requestDto, Long footId, User user) {
//        confirmAdminToken(user);

        ProductSize productSize = productSizeRepository.findById(footId).get();

        productSize.updateFootSize(requestDto);
    }

    public List<ProductResponseDto> getSearch(String name) {
        List<ProductResponseDto> productResponseDtos = new ArrayList<>(); // cursor 페이지네이션

        List<Product> products = productRepository.searchProduct(name);
        for (Product product : products) {
            productResponseDtos.add(new ProductResponseDto(product));
        }
        return productResponseDtos;
    }


    private void confirmAdminToken(User user){
        if (user.getRole().equals(UserRoleEnum.ADMIN)) {
        } else {
            throw new IllegalArgumentException("어드민유저만 접근할수 있습니다");
        }
    }
}
