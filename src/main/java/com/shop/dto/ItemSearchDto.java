package com.shop.dto;

import com.shop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter
@ToString
public class ItemSearchDto {

    private String searchDateType;

    private ItemSellStatus searchSellStatus;

    //itemNm : 상품명, createBy : 작성자 ->조회
    private String searchBy;

    //searchBy 따라 itemNm : 상품명검색,
    //searchBy가 작성자 상품등록자 아아디로 검색
    private String searchQuery = "";

}
