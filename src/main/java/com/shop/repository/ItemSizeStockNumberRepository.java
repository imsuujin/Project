package com.shop.repository;

import com.shop.entity.ItemSizeStockNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemSizeStockNumberRepository extends JpaRepository<ItemSizeStockNumber, Integer> {

    @Query("select iss from ItemSizeStockNumber iss where  iss.item.id = :itemId")
    List<ItemSizeStockNumber> findByItemId(@Param("itemId")Long itemId);
}
