package com.shop.repository;

import com.shop.dto.ItemImgDto;
import com.shop.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);


    ItemImg findByItemIdAndRepimgYn(Long itemId, String repimgYn);

//    @Query("select i from ItemImg i where i.item.id = :itemId and  i.repimgYn = :repimgYn")
//    ItemImg findByItemIdAndRepimgYn2(@Param("itemId")Long itemId,@Param("repimgYn")String repimgYn);
}
