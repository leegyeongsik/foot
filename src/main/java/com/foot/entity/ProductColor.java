package com.foot.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "productcolors")
public class ProductColor {
    /**
     * 컬럼 - 연관관계 컬럼을 제외한 컬럼을 정의합니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount")
    private Long amount;

    /**
     * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
     */
    @Builder
    public ProductColor(Long amount , ProductSize productSize , ProductColorImg productColorImg){
        this.amount = amount;
        this.productSize = productSize;
        this.productColorImg = productColorImg;
    }

    /**
     * 연관관계 - Foreign Key 값을 따로 컬럼으로 정의하지 않고 연관 관계로 정의합니다.
     */

    @ManyToOne
    @JoinColumn(name = "productSizeId", nullable = false)
    private ProductSize productSize;

    @ManyToOne
    @JoinColumn(name = "productColorImgId", nullable = false)
    private ProductColorImg productColorImg;
    /**
     * 연관관계 편의 메소드 - 반대쪽에는 연관관계 편의 메소드가 없도록 주의합니다.
     */


    /**
     * 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
     * ex (update 메소드)
     */
    public void updateProductColor(Long amount){
        this.amount = amount;
    }

    public void decreaseProductAmount(Long currentAmount , Long orderAmount){
        this.amount = currentAmount - orderAmount;
    }
}
