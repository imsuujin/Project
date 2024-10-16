package com.shop.dto;

import com.shop.constant.ItemSize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter@Setter
@ToString
public class ItemSizeStockDto {

    private Long id;

    @Enumerated(EnumType.STRING)
    private ItemSize itemSize;

    private int stockNumber;
}
