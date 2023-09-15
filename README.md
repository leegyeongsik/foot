## 판짱마켓 신발판매(owner) 및 신발경매(user) 사이트

## URL https://panchammarket.shop/

## 팀원 , 역할

| 이름 | 백엔드 | 프론트엔드|
|--|--|--|
|김준영|신발경매|신발경매
|유수인|회원가입 , 로그인 , 백오피스| 회원가입 , 로그인 , 백오피스 , 신발경매
|이경식|신발판매 , 채팅| 신발판매 , 채팅



## 주요 기능

- **신발판매** : 사이트에서 주관해서 신발을 판매하고 이용자들은 그 신발들을 수량을 선택해서 장바구니에 담고 장바구니에 담긴 상품들을 주문할수있음
  
- **채팅** : 유저가 질문이나 , 문제 등을 채팅으로 관리자와 실시간으로 소통을 할수있음
  
- **경매** : 유저가 경매상품을 등록하고 등록한 상품을 타유저가 정해진 시간이 지날때까지 입찰을 할수있고 시간이 지나면 제일 높은 입찰을 진행한 유저가 낙찰을 받음
  
- **회원가입/로그인** : 유저는 회원가입을 할수있고 로그인을해서 경매 , 상품주문 , 채팅 , 찜하기를 할수있음

## 메인 로직 1 : 경매


## 메인 로직 2 : 채팅
<img width="100%" src="https://github.com/leegyeongsik/foot/assets/67450537/ec087927-2c49-479d-bcb1-2176db2c8592"/>

## 기술 스택
springboot , jpa , querydsl , kafka , mysql , aws(ec2 , rds , s3)
## 개발 기간
- 2023-08-16 ~ 2023-09-15
## 기획 & 설계
[기능 명세서](https://www.notion.so/facd780f03d04beab52f85c3016e4e66)

[와이어프레임](https://www.figma.com/file/dfSJiL0lQcxdCyz1fOIhIu/foot?type=design&node-id=0-1&mode=design&t=Zzfa3tOCrYX33Qze-0)

[API 명세서](https://s-organization-175.gitbook.io/foot/reference/api-reference/user)

[ERD](https://www.erdcloud.com/d/vcrjsLChyxNeQPgiB)
