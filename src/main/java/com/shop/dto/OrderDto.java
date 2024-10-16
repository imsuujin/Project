package com.shop.dto;

import com.shop.constant.ItemSize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
public class OrderDto {

    @NotNull(message="상품 아이디는 필수 입력 값입니다")
    private Long itemId;

    @Min(value = 1, message = "최소 주문 수량은 1개 입니다.")

    @Max(value = 99, message="최대 주문수량은 99개 입니다.")
    private int count;

    private ItemSize itemSize;
}
