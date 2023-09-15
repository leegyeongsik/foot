package com.foot.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "products")
public class Product extends Timestamped {
    /**
     * 컬럼 - 연관관계 컬럼을 제외한 컬럼을 정의합니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "total_amount")
    private Long TotalAmount;
    @Column(name = "description")
    private String Description;
    @Column(name = "price")
    private Long price;
    @Column(name = "model")
    private String model;
    @Column(name = "modelpicture")
    private String modelpicture;

    @Column(name = "discountrate" )
    @ColumnDefault("0")
    private double discountRate;

    @Column(name = "discountprice")
    private Long discountPrice;

    @Builder
    public Product(Long TotalAmount , String Description , Long price  , String model , String modelpicture , User user , Long discountPrice ,Brand brand){
        this.TotalAmount = TotalAmount;
        this.Description =Description;
        this.price =price;
        this.model = model;
        this.modelpicture = modelpicture;
        this.user = user;
        this.discountPrice = discountPrice;
        this.brand = brand;
    }


    /**
     * 연관관계 - Foreign Key 값을 따로 컬럼으로 정의하지 않고 연관 관계로 정의합니다.
     */
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "brandId")
    private Brand brand;


    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<ProductSize> productSizes = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<OrderHistory> orderHistories = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<Favorite> Favorites = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<ProductColorImg> productColorImgs = new ArrayList<>();

    /**
     * 연관관계 편의 메소드 - 반대쪽에는 연관관계 편의 메소드가 없도록 주의합니다.
     */


    /**
     * 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
     * ex (update 메소드)
     */

    public void updateProduct(String name , String description , Long price , Long totalAmount){
        this.model = name;
        this.price = price;
        this.Description = description;
        this.TotalAmount = totalAmount;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public void setDiscountPrice(Long discountPrice) {
        this.discountPrice = discountPrice;
    }

    public void decreaseProductAmount(Long currentAmount , Long orderAmount){
        this.TotalAmount = currentAmount - orderAmount;
    }

}
