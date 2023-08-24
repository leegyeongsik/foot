package com.foot.entity;

import com.foot.dto.BidProductRequestDto;
import jakarta.persistence.*;
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
    private LocalDateTime expirationPeriod;

    @Column(name = "start_price" , nullable = false)
    private Long startPrice;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "footpicture")
    private String footpicture;

    @Column(name = "predictfootpicture")
    private String predictfootpicture;

    @Column(name = "footsize")
    private Long footsize;

    @Column(name = "feetsize")
    private Long feetsize;

    // status가 0이면 진행중 1이면 마감됨
    @Column(name = "status")
    private int status;


    /**
     * 연관관계 - Foreign Key 값을 따로 컬럼으로 정의하지 않고 연관 관계로 정의합니다.
     */


    @OneToMany(mappedBy = "bidProduct", cascade = CascadeType.REMOVE)
    private List<Bid> bids = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "brandId", nullable = false)
    private Brand brand;


    /**
     * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
     */

    public BidProduct(BidProductRequestDto requestDto, Brand brand) {
        this.expirationPeriod = requestDto.getExpirationPeriod();
        this.startPrice = requestDto.getStartPrice();
        this.name = requestDto.getName();
        this.description = requestDto.getDescription();
        this.footpicture = requestDto.getFootPicture();
        this.footsize = requestDto.getFootSize();
        this.feetsize = requestDto.getFeetSize();
        this.brand = brand;
        this.status = 0;
    }

    public void update(BidProductRequestDto requestDto) {
        this.expirationPeriod = requestDto.getExpirationPeriod();
        this.startPrice = requestDto.getStartPrice();
        this.name = requestDto.getName();
        this.description = requestDto.getDescription();
        this.footpicture = requestDto.getFootPicture();
        this.footsize = requestDto.getFootSize();
        this.feetsize = requestDto.getFeetSize();
    }

    public void changeToSell() {
        this.status = 1;
    }


    /**
     * 연관관계 편의 메소드 - 반대쪽에는 연관관계 편의 메소드가 없도록 주의합니다.
     */


    /**
     * 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
     * ex (update 메소드)
     */
}