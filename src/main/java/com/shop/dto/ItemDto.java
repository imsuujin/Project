package com.shop.dto;

import com.shop.constant.ItemSellStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

    private Long id;

    private String itemNm;

    private  int price;

    private String itemDetail;

    private String sellStatCd;

    private LocalDateTime regTime;

    private LocalDateTime updateTime;


}
