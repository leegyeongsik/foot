package com.foot.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "productcolorImg")
public class ProductColorImg {
    /**
     * 컬럼 - 연관관계 컬럼을 제외한 컬럼을 정의합니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "colorimg")
    private String colorimg;
    @Column(name = "colorname")
    private String colorname;

    @Builder
    public ProductColorImg(String colorimg,String colorname ,Product product ){
        this.colorimg = colorimg;
        this.colorname =colorname;
        this.product = product;
    }

    /**
     * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
     */


    /**
     * 연관관계 - Foreign Key 값을 따로 컬럼으로 정의하지 않고 연관 관계로 정의합니다.
     */

    @OneToMany(mappedBy = "productColorImg", cascade = CascadeType.REMOVE)
    private List<ProductColor> productColors = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    private Product product;


    /**
     * 연관관계 편의 메소드 - 반대쪽에는 연관관계 편의 메소드가 없도록 주의합니다.
     */


    /**
     * 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
     * ex (update 메소드)
     */
}
