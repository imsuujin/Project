package com.shop.dto;

import com.shop.constant.ItemSize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class CartDetailDto {

    private Long cartItemId; //장바구니 상품 아이디

    private String itemNm; //상품명

    private int price; //상품 금액

    private int count; //수량

    private String imgUrl; //상품 이미지 경로

    private Long itemId; //아이템 아이디

    private ItemSize itemSize;

    public CartDetailDto(Long cartItemId, String itemNm, int price, int count, String imgUrl,Long itemId, ItemSize itemSize){
        this.cartItemId = cartItemId;
        this.itemNm = itemNm;
        this.price = price;
        this.count = count;
        this.imgUrl = imgUrl;
        this.itemId = itemId;
        this.itemSize = itemSize;
    }

}