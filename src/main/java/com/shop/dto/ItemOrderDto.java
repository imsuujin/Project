package com.shop.dto;

import com.shop.constant.ItemSize;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ItemOrderDto {

    private Long id;

    private String itemNm;

    private Integer price;

    private ItemSize itemSize;

    private String imgUrl;

    private int count;
}
