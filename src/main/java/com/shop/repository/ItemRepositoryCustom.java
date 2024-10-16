package com.shop.repository;

import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    //                                        상품 조회 조건 ,  페이징 정보
    Page<Item> getAdiminItemPage(ItemSearchDto itemsearchDto, Pageable pageable);

    Page<MainItemDto> getMainItemPage(ItemSearchDto itemsearchDto, Pageable pageable);
}
