package com.foot.service;

import com.foot.dto.OrderRequestDto;
import com.foot.dto.bidProduct.BrandResponseDto;
import com.foot.dto.products.*;
import com.foot.entity.*;
import com.foot.exception.OutOfProductException;
import com.foot.repository.BrandRepository;
import com.foot.repository.OrderHistoryRepository;
import com.foot.repository.UserRepository;
import com.foot.repository.cart.CartItemRepository;
import com.foot.repository.favorite.FavoriteRepository;
import com.foot.repository.products.ProductColorImgRepository;
import com.foot.repository.products.ProductColorRepository;
import com.foot.repository.products.ProductRepository;
import com.foot.repository.products.ProductSizeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductColorImgRepository productColorImgRepository;
    private final ProductColorRepository productColorRepository;
    private final ProductSizeRepository productSizeRepository;
    private final S3UploadService s3UploadService;
    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final OrderHistoryRepository orderHistoryRepository;
    private final CartService cartService;
    private final BrandRepository brandRepository;

    @Transactional
    public void createProduct(ProductRequestDto requestDto, User user) throws IOException { // 상품등록
        List<MultipartFile> modelColorImg = requestDto.getModelColorImg(); // 컬러 이미지
        List<String> modelColorName = requestDto.getModelColorName(); // 컬러 네임

        List<Long> modelAmount = requestDto.getModelAmount(); // 상품의 총 개수
        List<Long> modelSize = requestDto.getModelSize(); // 상품의 사이즈

        List<Long> modelFootSize = requestDto.getModelFootSize(); // 사이즈의 발사이즈
        List<Long> modelFeetSize = requestDto.getModelFeetSize(); // 사이즈의 발볼사이즈


        HashMap<Integer, ArrayList<String>> modelColor = requestDto.getModelColor(); // 상품의 색상이름
        HashMap<Integer, ArrayList<Long>> modelColorAmount = requestDto.getModelColorAmount(); // 색상의 개수

        Brand brand = brandRepository.findByName(requestDto.getBrand());

        Product product = Product.builder() // 모델 생성
                .model(requestDto.getName())
                .price(requestDto.getPrice())
                .TotalAmount(requestDto.getTotalAmount())
                .Description(requestDto.getDescription())
                .modelpicture(s3UploadService.uploadImage(requestDto.getModelPicture())) // 들어온거 업로드해서 주소 넣어줌
                .user(user)
                .discountPrice(requestDto.getPrice())
                .brand(brand)
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

    public ProductsResponseDto getProduct() { // 전체 상품 조회 // true 인지 false인지
        List<ProductResponseDto> productResponseDtos = new ArrayList<>();
        List<BrandResponseDto> brandResponseDtos = new ArrayList<>();

        List<Product> products = productRepository.findAll();
        for (Product product : products) {
            productResponseDtos.add(new ProductResponseDto(product));
        }
        List<Brand> brands = brandRepository.findAll();
        for (Brand brand : brands) {
            brandResponseDtos.add(new BrandResponseDto(brand));
        }

        return new ProductsResponseDto(productResponseDtos, brandResponseDtos);
    }

    public ProductsResponseDto getUserProduct(User user) {
        List<ProductResponseDto> productResponseDtos = new ArrayList<>();
        List<BrandResponseDto> brandResponseDtos = new ArrayList<>();

        List<Product> products = productRepository.findAll();
        for (Product product : products) {
            Optional<Favorite> favorite = favoriteRepository.getFavoriteIsExist(user.getId(), product.getId());
            if (!favorite.isEmpty()) {
                productResponseDtos.add(new ProductResponseDto(product, true));
            } else {
                productResponseDtos.add(new ProductResponseDto(product));
            }
        }

        List<Brand> brands = brandRepository.findAll();
        for (Brand brand : brands) {
            brandResponseDtos.add(new BrandResponseDto(brand));
        }

        return new ProductsResponseDto(productResponseDtos, brandResponseDtos);
    }

    public innerProductResponseDto getTargetProduct(Long productId) { // 특정 상품 조회
        List<ProductColorResponseDto> productColorResponseDtos = new ArrayList<>();
        Product product = productRepository.findById(productId).get();

        List<ProductColorImg> productColorImgs = productRepository.getModelColors(productId);
        for (ProductColorImg colorImg : productColorImgs) {
            List<ProductSizeResponseDto> productSizeResponseDtos = new ArrayList<>();
            Map<ProductSize, ProductColor> sizeProductColorMap =
                    productRepository.getModelSizeOfColor(productId, colorImg.getId());
            for (ProductSize productSize : sizeProductColorMap.keySet()) {
                productSizeResponseDtos.add(new ProductSizeResponseDto(productSize, sizeProductColorMap.get(productSize)));
            }
            productColorResponseDtos.add(new ProductColorResponseDto(colorImg, productSizeResponseDtos));
        }

        List<ProductSizesResponseDto> productSizesResponseDtos = new ArrayList<>(); // 상품의 사이즈
        List<ProductSize>productSizes=productRepository.getSizes(productId);
        for (ProductSize productSize : productSizes) {
            productSizesResponseDtos.add(new ProductSizesResponseDto(productSize));
        }

        return new innerProductResponseDto(new ProductResponseDto(product), productColorResponseDtos , productSizesResponseDtos);
    }

    public innerProductResponseDto getTargetUserProduct(Long productId, User user) {
        List<ProductColorResponseDto> productColorResponseDtos = new ArrayList<>();
        Product product = productRepository.findById(productId).get();
        List<ProductColorImg> productColorImgs = productRepository.getModelColors(productId);
        for (ProductColorImg colorImg : productColorImgs) {
            List<ProductSizeResponseDto> productSizeResponseDtos = new ArrayList<>();
            Map<ProductSize, ProductColor> sizeProductColorMap =
                    productRepository.getModelSizeOfColor(productId, colorImg.getId());
            for (ProductSize productSize : sizeProductColorMap.keySet()) {
                productSizeResponseDtos.add(new ProductSizeResponseDto(productSize, sizeProductColorMap.get(productSize)));
            }
            productColorResponseDtos.add(new ProductColorResponseDto(colorImg, productSizeResponseDtos));
        }

        List<ProductSizesResponseDto> productSizesResponseDtos = new ArrayList<>(); // 상품의 사이즈
        List<ProductSize>productSizes=productRepository.getSizes(productId);
        for (ProductSize productSize : productSizes) {
            productSizesResponseDtos.add(new ProductSizesResponseDto(productSize));
        }
        Optional<Favorite> favorite = favoriteRepository.getFavoriteIsExist(user.getId(), product.getId());
        if (!favorite.isEmpty()) {
            return new innerProductResponseDto(new ProductResponseDto(product, true), productColorResponseDtos , productSizesResponseDtos);
        } else {
            return new innerProductResponseDto(new ProductResponseDto(product), productColorResponseDtos , productSizesResponseDtos);
        }
    }

    public innerProductResponseDto getSizeProduct(Long productId) { //수정할때 사이즈 , 사이즈안의 컬러 불러오는 메소드
        Product product = productRepository.findById(productId).get();
        List<ProductSizeResponseDto> productSizeResponseDtos = new ArrayList<>();

        List<ProductSize> productSizes = productRepository.getModelSize(productId);
        for (ProductSize productSize : productSizes) {
            List<ProductColorResponseDto> productColorResponseDtos = new ArrayList<>();
            Map<ProductColor, ProductColorImg> sizeProductColor =
                    productRepository.getSizeColorProduct(productId, productSize.getId());

            for (ProductColor productColor : sizeProductColor.keySet()) {
                productColorResponseDtos.add(new ProductColorResponseDto(productColor, sizeProductColor.get(productColor)));
            }
            productSizeResponseDtos.add(new ProductSizeResponseDto(productSize, productColorResponseDtos));

        }
        return new innerProductResponseDto(new ProductResponseDto(product), productSizeResponseDtos);
    }

    @Transactional
    public void updateProduct(Long productId, UpdateProductResponseDto updateProductResponseDto) {

        Product product = productRepository.findById(productId).get();
        product.updateProduct(updateProductResponseDto.getName(), updateProductResponseDto.getDescription(),
                updateProductResponseDto.getPrice(), updateProductResponseDto.getTotalAmount());
        productRepository.save(product);

        HashMap<Integer, ArrayList<Long>> modelColorAmount = updateProductResponseDto.getProductColorAmount();
        for (Integer integer : modelColorAmount.keySet()) {
            List<Long> ColorAmount = modelColorAmount.get(integer);
            ProductColor productColor = productColorRepository.findById(ColorAmount.get(0)).get();
            productColor.updateProductColor(ColorAmount.get(1));

            productColorRepository.save(productColor);
        }
        HashMap<Integer, ArrayList<Long>> modelProductSizeInfo = updateProductResponseDto.getProductSizeInfo();
        for (Integer integer : modelProductSizeInfo.keySet()) {
            List<Long> SizeInfo = modelProductSizeInfo.get(integer);
            ProductSize productSize = productSizeRepository.findById(SizeInfo.get(0)).get();
            productSize.updateProductSize(SizeInfo.get(1), SizeInfo.get(2), SizeInfo.get(3), SizeInfo.get(4));
            productSizeRepository.save(productSize);

        }


    }

    @Transactional
    public void deleteProduct(Long productId) {
//        confirmAdminToken(user);

        Product product = productRepository.findById(productId).get();
        s3UploadService.deleteFile(product.getModelpicture());
        List<ProductColorImg> productColorImgs = productRepository.getModelColors(productId);
        for (ProductColorImg colorImg : productColorImgs) {
            s3UploadService.deleteFile(colorImg.getColorimg());
        }
        productRepository.delete(product);
    }


    public List<ProductResponseDto> getSearch(String name) {
        List<ProductResponseDto> productResponseDtos = new ArrayList<>(); // cursor 페이지네이션

        List<Product> products = productRepository.searchProduct(name);
        for (Product product : products) {
            productResponseDtos.add(new ProductResponseDto(product));
        }
        return productResponseDtos;
    }

    /**
     * requestDto라는 데이터를 받고
     * dto에 있는 productcolorid로 해당 상품의 productcolor , 와 productsize를 받아옴
     * 받아온 정보로 requestdto에서 받아온 상품 주문 수량을
     * 해당 사이즈 재고와 , 해당 사이즈의 색상 재고 상품의 총 재고를 각각 빼주고 db에 save해줌
     * 각 정보들로 orderhistory에 데이터를 save해주고 장바구니에 담겨있던 상품을 삭제해준다
     *
     * @thorows 만약 사이즈의 컬러재고가 0보다 작다면 exception 보내서 해당 컬러의 재고가 존재하지않습니다
     */
    @Transactional
    public void OrderProduct(OrderRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getOrderUserId()).get(); // 데이터로 user를 받고
        Map<ProductSize, ProductColor> sizeProductColor = productRepository.getOrderProduct(requestDto.getOrderProductcolorId());
        ProductSize productSize = (ProductSize) sizeProductColor.keySet().toArray()[0];
        ProductColor productColor = sizeProductColor.get(productSize);

        productColor.decreaseProductAmount(productColor.getAmount() , requestDto.getOrderCount());
        productSize.decreaseProductAmount(productSize.getAmount() , requestDto.getOrderCount());
        productSize.getProduct().decreaseProductAmount(productSize.getProduct().getTotalAmount() , requestDto.getOrderCount());


        productRepository.save(productSize.getProduct());
        productSizeRepository.save(productSize);
        productColorRepository.save(productColor);

        OrderHistory orderHistory = OrderHistory.builder().user(user)
                .product(productSize.getProduct())
                .color(productColor.getProductColorImg().getColorname())
                .size(productSize.getSize())
                .amount(requestDto.getOrderCount() * requestDto.getOrderPrice())
                .totalCnt(requestDto.getOrderCount())
                .build();

        orderHistoryRepository.save(orderHistory);
        cartService.deleteCartItem(requestDto.getOrderCartItemId());
    }

    public List<ProductResponseDto> getCategoryProduct(String brand) {
        List<ProductResponseDto> productResponseDtos = new ArrayList<>();

        List<Product> products = productRepository.getBrandProduct(brand);
        for (Product product : products) {
            productResponseDtos.add(new ProductResponseDto(product));
        }
        return productResponseDtos;
    }

    public List<ProductResponseDto> getCategoryUserProduct(String brand, User user) {

        List<ProductResponseDto> productResponseDtos = new ArrayList<>();

        List<Product> products = productRepository.getBrandProduct(brand);
        for (Product product : products) {
            Optional<Favorite> favorite = favoriteRepository.getFavoriteIsExist(user.getId(), product.getId());
            if (!favorite.isEmpty()) {
                productResponseDtos.add(new ProductResponseDto(product, true));
            } else {
                productResponseDtos.add(new ProductResponseDto(product));
            }
        }
        return productResponseDtos;
    }
}
