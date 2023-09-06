package com.foot.entity;

import com.foot.dto.products.FootProductRequestDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "productsizes")
public class ProductSize extends Timestamped{
    /**
     * 컬럼 - 연관관계 컬럼을 제외한 컬럼을 정의합니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "size")
    private Long size;
    @Column(name = "amount")
    private Long amount;

    @Column(name = "footpicture")
    private String footpicture;
    @Column(name = "predictfootpicture")
    private String predictfootpicture;
    @Column(name = "footsize")
    private Long footsize;
    @Column(name = "feetsize")
    private Long feetsize;



    /**
     * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
     */
    @Builder
    public ProductSize(Long size , Long amount ,Product product , Long footsize ,Long feetsize , String footpicture ,  String predictfootpicture){
        this.size = size;
        this.amount =amount;
        this.product = product;
        this.footsize = footsize;
        this.feetsize = feetsize;
        this.footpicture = footpicture;
        this.predictfootpicture = predictfootpicture;
    }

    /**
     * 연관관계 - Foreign Key 값을 따로 컬럼으로 정의하지 않고 연관 관계로 정의합니다.
     */
    @OneToMany(mappedBy = "productSize", cascade = CascadeType.REMOVE)
    private List<ProductColor> productColors = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    private Product product;

    public void updateFootSize(FootProductRequestDto requestDto) {
        this.footsize = requestDto.getFootSize();
        this.feetsize = requestDto.getFeetSize();
        this.footpicture = requestDto.getFootPicture();
    }

    /**
     * 연관관계 편의 메소드 - 반대쪽에는 연관관계 편의 메소드가 없도록 주의합니다.
     */


    /**
     * 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
     * ex (update 메소드)
     */
    public void updateProductSize(Long size , Long amount , Long footsize , Long feetsize){
        this.size =size;
        this.amount = amount;
        this.footsize = footsize;
        this.feetsize = feetsize;
    }

}
