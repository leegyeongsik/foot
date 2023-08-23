package com.foot.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "bidproduct")
public class BidProduct extends Timestamped{
    /**
     * 컬럼 - 연관관계 컬럼을 제외한 컬럼을 정의합니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "expiration_period" , nullable = false)
    private LocalDateTime ExpirationPeriod;

    @Column(name = "start_price" , nullable = false)
    private Long startPrice;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;


    /**
     * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
     */
    @Builder
    public BidProduct(LocalDateTime ExpirationPeriod , Long startPrice , String name , String description  ,User user , Brand brand){
        this.ExpirationPeriod = ExpirationPeriod;
        this.startPrice =startPrice;
        this.name =name;
        this.description = description;
        this.user = user;
        this.brand = brand;
    }


    /**
     * 연관관계 - Foreign Key 값을 따로 컬럼으로 정의하지 않고 연관 관계로 정의합니다.
     */


    @OneToMany(mappedBy = "bidProduct", cascade = CascadeType.REMOVE)
    private List<Bid> bids = new ArrayList<>();

    @OneToMany(mappedBy = "bidProduct", cascade = CascadeType.REMOVE)
    private List<BidProductFoot> bidProductFoots = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "brandId", nullable = false)
    private Brand brand;

    /**
     * 연관관계 편의 메소드 - 반대쪽에는 연관관계 편의 메소드가 없도록 주의합니다.
     */


    /**
     * 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
     * ex (update 메소드)
     */
}
