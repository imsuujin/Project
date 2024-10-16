package com.shop.service;

import com.shop.dto.ItemSizeStockDto;
import com.shop.entity.ItemSizeStockNumber;
import com.shop.repository.ItemSizeStockNumberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class ItemSizeStockService {

    private final ItemSizeStockNumberRepository stockNumberRepository;

    public List<ItemSizeStockDto> getItemSizeStockNumber(Long itemId) {
        List<ItemSizeStockNumber> itemSizeStockList = stockNumberRepository.findByItemId(itemId);

        log.info(itemSizeStockList);

        List<ItemSizeStockDto> itemSizeStockDtoList = new ArrayList<>();
        for (ItemSizeStockNumber itemSizeStockNumber : itemSizeStockList) {
            ItemSizeStockDto itemSizeStockDto = new ItemSizeStockDto();
            itemSizeStockDto.setId(itemSizeStockNumber.getId());
            itemSizeStockDto.setItemSize(itemSizeStockNumber.getItemSize());
            itemSizeStockDto.setStockNumber(itemSizeStockNumber.getStockNumber());

            itemSizeStockDtoList.add(itemSizeStockDto);
        }
        log.info("서비스 마지막 테스트"+itemSizeStockDtoList);
        return itemSizeStockDtoList;
    }
}