package com.shop.dto;

import com.shop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class OrderItemDto {

    private String itemNm;
    private int count;
    private int orderPrice;
    private String imgUrl; //상품 이미지 경로


    public OrderItemDto(OrderItem orderItem, String imgUrl) {
        this.itemNm = orderItem.getItem().getItemNm();
        this.count = orderItem.getCount();
        this.imgUrl = imgUrl;
        this.orderPrice = orderItem.getOrderPrice();

    }
}
