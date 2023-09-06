package com.foot.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Columns;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "users")
public class User {

    /**
     * 컬럼 - 연관관계 컬럼을 제외한 컬럼을 정의합니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email" ,unique = true)
    private String email;

    @Column(name = "name" , nullable = false)
    private String name;

    @Column(name = "password" , nullable = false)
    private String password;

    @Column(name = "address" , nullable = false)
    private String address;

    @Column(name = "cellphone" , nullable = false)
    private String cellphone;

    @Column(name = "userImage")
    private String userImage;

    @Enumerated(value = EnumType.STRING) // enum 타입을 데이터베이스에 저장할때 사용하는 애너테이션
    private UserRoleEnum role;

//    private Long kakaoId; 쓸지안쓸지 모르겠음

    /**
     * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
     */
    @Builder
    public User(String email , String name , String password , String address , String cellphone , UserRoleEnum role , String userImage){
        this.email = email;
        this.address = address;
        this.cellphone = cellphone;
        this.name = name;
        this.password = password;
        this.role = role;
        this.userImage = userImage;
    }

    /**
     * 연관관계 - Foreign Key 값을 따로 컬럼으로 정의하지 않고 연관 관계로 정의합니다.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE) // 다 삭제해줄지 아니면 참조된거 null로 바꿔서 남겨줄지 고민 나중에 더 생각해보고 이렇게 많은게 맞나 생각
    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<UserFoot> userFoots = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<OrderHistory> orderHistories = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Favorite> Favorites = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<BidProduct> bidProducts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Bid> bids = new ArrayList<>();

    /**
     * 연관관계 편의 메소드 - 반대쪽에는 연관관계 편의 메소드가 없도록 주의합니다.
     */


    /**
     * 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
     * ex (update 메소드)
     */
}
