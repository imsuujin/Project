package com.shop.dto;

import com.shop.constant.ItemSellStatus;
import com.shop.constant.ItemSize;
import com.shop.constant.ItemType;
import com.shop.entity.Item;
import lombok.*;
import org.modelmapper.ModelMapper;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ItemFormDto {

    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemNm;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotBlank(message = "상품 상세는 필수 입력 값입니다.")
    private String itemDetail;

//    @NotNull(message = "재고는 필수 입력 값입니다.")
//    private Integer stockNumber;
    @NotNull(message="상품 종류는 필수 입력 값입니다.")
    //    @Enumerated(EnumType.STRING)//열거형 타입
    private ItemType itemType;

    private String itemTypeDetail;

//    @Enumerated(EnumType.STRING)
    private ItemSize itemSize;

    private int sizeXXL;

    private int sizeXL;

    private int sizeL;

    private int sizeM;

    private int sizeS;

//    @Enumerated(EnumType.STRING)//열거형 타입.. 상수 그룹화
    private ItemSellStatus itemSellStatus;

    private float rating;

    private int ratingCount;

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    private List<Long> itemImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    //ItemFormDto 객체(this)를 Item 클래스의 객체로 변환
    public Item createItem(){
        return modelMapper.map(this, Item.class);
    }

    public static ItemFormDto of(Item item){

        return modelMapper.map(item, ItemFormDto.class);
    }

}