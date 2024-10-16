package com.shop.entity;

import com.shop.constant.ItemSize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "order_item")
@ToString
@Setter
@Getter
public class OrderItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")

    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")  //외래키 설정
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Item item;


    private int orderPrice; //가격
    private int count; //  구매 할 수량

    private ItemSize itemSize;//고른 사이즈

    public static OrderItem createOrderItem(Item item, int count, ItemSize itemSize) {
        OrderItem orderItem = new OrderItem();

        orderItem.setItem(item);
        orderItem.setCount(count);


        orderItem.setOrderPrice(item.getPrice());

        ItemSizeStockNumber itemSizeStockNumber = new ItemSizeStockNumber();
        itemSizeStockNumber.removeStock(count);

        orderItem.setItemSize(itemSize);

        return orderItem;
    }
    public int getTotalPrice(){
        return orderPrice*count;
    }

    public void cancel(){
    }
}
