package com.foot.dto.products;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
public class ProductRequestDto {
    String name;
    Long totalAmount;
    String description;
    Long price;
    MultipartFile ModelPicture; // product

    List<MultipartFile> modelColorImg;
    List<String> modelColorName; // productcolors

    List<Long> modelAmount;
    List<Long> modelSize;

    List<Long> modelFootSize;
    List<Long> modelFeetSize; // productsize

    HashMap<Integer, ArrayList<String>> modelColor = new HashMap<>(); // 키: 1++ 벨류 : 사이즈에 존재하는 컬러 네임
    HashMap<Integer, ArrayList<Long>> modelColorAmount = new HashMap<>(); // 키: 1++ 벨류 : 사이즈에 존재하는 각컬러의 남은 개수  // productcolor

}
