package com.shop.entity;

import com.shop.constant.ItemSellStatus;
import com.shop.constant.ItemType;
import com.shop.dto.ItemFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@ToString
@Entity
@Table(name = "item")
public class Item extends BaseEntity {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String itemNm;   //item_Nm

    @Column(name = "price", nullable = false)
    private  int price;   //price



    @Lob //Large Object를 매핑하는데 사용됨. Large Object는 일반적으로 데이터베이스에 저장될 때 크기가 큰 데이터 객체를 의미.. 컬럼 타입 == longtext
    @Column(nullable = false)
    private String itemDetail;

    //sale퍼센트
    private int sale;

    //현재 평점
    private float rating;
    //평점 참여한 인원 수
    private int ratingCount;

    @Enumerated(EnumType.STRING)//열거형 타입.. 상수 그룹화
    private ItemSellStatus itemSellStatus; //Order, Cancel

    //상의 하의 가방 기타
    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    private String itemTypeDetail;

    public void updateRating(float rating){ //평점 계산...
        float tmpRating = this.rating * this.ratingCount;
        this.ratingCount++;
        this.rating = tmpRating + rating / this.ratingCount;
    }

    public void deleteRating(float rating){
        float tmpRating = this.rating * this.ratingCount;
        this.ratingCount--;
        this.rating = tmpRating - rating / this.ratingCount;
    }

    public void updateItem(ItemFormDto itemFormDto){
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
//        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }



    //상품 재고 수량 변경
//    public void removeStock(int stockNumber){
//        int restStock = this.stockNumber - stockNumber;
//        if(restStock < 0){
//            throw new OutOfStockException("상품의 재고가 부족합니다." +
//                    "현재 재고 수량 :" + this.stockNumber);
//        }
//        this.stockNumber = restStock;
//    }

    //상품 재고 취소시
    public void addStock(int stockNumber, String itemSize){
        ItemSizeStockNumber itemSizeStockNumber = new ItemSizeStockNumber();
        itemSizeStockNumber.addStock(stockNumber);
    }

}
