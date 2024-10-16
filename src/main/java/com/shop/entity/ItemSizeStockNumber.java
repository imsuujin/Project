package com.shop.entity;

import com.shop.constant.ItemSize;
import com.shop.exception.OutOfStockException;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ItemSizeStockNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    Item item;

    @Column(nullable = false)//null값 허용 안함
    private  int stockNumber; //stoack_number

    //Ex:ItemSize: XL, L, M
    @Enumerated(EnumType.STRING)
    private ItemSize itemSize;

    //상품 재고 수량 변경
    public void removeStock(int stockNumber) {
        int restStock = this.stockNumber - stockNumber;
        if(restStock < 0){
            throw new OutOfStockException("상품의 재고가 부족합니다." +
                    "현재 재고 수량 :" + this.stockNumber);
       }
        this.stockNumber = restStock;
    }

    //상품 재고 취소시
    public void addStock(int stockNumber) {
        int restStock = this.stockNumber + stockNumber;
    }

}
