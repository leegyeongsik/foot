package com.foot.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "bidproductfoot")
public class BidProductFoot extends Timestamped{
    /**
     * 컬럼 - 연관관계 컬럼을 제외한 컬럼을 정의합니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    public BidProductFoot(String footpicture , String predictfootpicture , Long footsize , Long feetsize , BidProduct bidProduct){
        this.feetsize = feetsize;
        this.footsize = footsize;
        this.footpicture = footpicture;
        this.predictfootpicture = predictfootpicture;
        this.bidProduct = bidProduct;
    }


    /**
     * 연관관계 - Foreign Key 값을 따로 컬럼으로 정의하지 않고 연관 관계로 정의합니다.
     */
    @ManyToOne
    @JoinColumn(name = "bidproductId", nullable = false)
    private BidProduct bidProduct;


    /**
     * 연관관계 편의 메소드 - 반대쪽에는 연관관계 편의 메소드가 없도록 주의합니다.
     */


    /**
     * 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
     * ex (update 메소드)
     */
}