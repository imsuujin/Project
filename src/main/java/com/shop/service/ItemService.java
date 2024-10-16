package com.shop.service;

import com.shop.constant.ItemSize;
import com.shop.dto.*;
import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import com.shop.entity.ItemSizeStockNumber;
import com.shop.repository.CartItemRepository;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
import com.shop.repository.ItemSizeStockNumberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemSizeStockNumberRepository stockNumberRepository;
    private final ItemImgRepository itemImgRepository;
    private final FileService fileService;
    private final CartItemRepository cartItemRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws  Exception{
        log.info("itemFormDto:" + itemFormDto);
        //상품 등록
        Item item = itemFormDto.createItem();
        itemRepository.save(item);
        Item findItem = itemRepository.findItemByItemNm(item.getItemNm());
        int XXL= itemFormDto.getSizeXXL();
        int XL= itemFormDto.getSizeXL();
        int L= itemFormDto.getSizeL();
        int M= itemFormDto.getSizeM();
        int S= itemFormDto.getSizeS();
        Long itemId = findItem.getId();
        log.info("itemId:" + itemId);
        //XXL
        ItemSizeStockNumber itemSizeStockNumber = ItemSizeStockNumber.builder()
                .itemSize(ItemSize.XXL)
                .item(findItem)
                .stockNumber(XXL)
                .build();
        stockNumberRepository.save(itemSizeStockNumber);

        //XL
        ItemSizeStockNumber itemSizeStockNumber1 = ItemSizeStockNumber.builder()
                .itemSize(ItemSize.XL)
                .item(findItem)
                .stockNumber(XL)
                .build();
        stockNumberRepository.save(itemSizeStockNumber1);

        //L
        ItemSizeStockNumber itemSizeStockNumber2 = ItemSizeStockNumber.builder()
                .itemSize(ItemSize.L)
                .item(findItem)
                .stockNumber(L)
                .build();
        stockNumberRepository.save(itemSizeStockNumber2);

        //M
        ItemSizeStockNumber itemSizeStockNumber3 = ItemSizeStockNumber.builder()
                .itemSize(ItemSize.M)
                .item(findItem)
                .stockNumber(M)
                .build();
        stockNumberRepository.save(itemSizeStockNumber3);

        //S
        ItemSizeStockNumber itemSizeStockNumber4 = ItemSizeStockNumber.builder()
                .itemSize(ItemSize.S)
                .item(findItem)
                .stockNumber(S)
                .build();
        stockNumberRepository.save(itemSizeStockNumber4);

        //이미지등록
        for(int i=0; i<itemImgFileList.size(); i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);

            if(i==0){
                itemImg.setRepimgYn("Y");  //첫번째 사진이 대표이미지
            }else{
                itemImg.setRepimgYn("N");
            }
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }
        return item.getId();
    }

    @Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long itemId){

        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);

        log.info("--------------itemImgList-----------------------");
        itemImgList.forEach(list-> log.info(list));

        List<ItemImgDto> itemImgDtoList = new ArrayList<>();

        //ItemImg(상품이미지) -> ItemImgDto 전달
        for( ItemImg itemImg: itemImgList){
            ItemImgDto itemImgDto = ItemImgDto.ItemImgOfItemImgDto(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        //Item(상품정보) -> ItemFormDto 전달
        Optional<Item> result = itemRepository.findById(itemId);
        Item item = result.orElseThrow(() -> new EntityNotFoundException());

        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);

        return itemFormDto;
    }

//    public List<ItemFormDto> getListItemDtl(List<Long> itemIds){
//
//        List<ItemImg> itemImgList = new ArrayList<>();
//        for(Long itemId: itemIds){
//            log.info("--------------itemId---------------------" + itemId);
//        itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
//        }
//        log.info("--------------itemImgList-----------------------");
//        itemImgList.forEach(list-> log.info(list));
//
//        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
//
//        //ItemImg(상품이미지) -> ItemImgDto 전달
//        for( ItemImg itemImg: itemImgList){
//            ItemImgDto itemImgDto = ItemImgDto.ItemImgOfItemImgDto(itemImg);
//            itemImgDtoList.add(itemImgDto);
//        }
//
//        //Item(상품정보) -> ItemFormDto 전달
//        List<Item> result = new ArrayList<>();
//        for(Long itemId: itemIds) {
//            result = itemRepository.findByIdIn(itemId);
//            Item item = result.orElseThrow(() -> new EntityNotFoundException());
//        }
//        ItemFormDto itemFormDto = ItemFormDto.of(result);
//        itemFormDto.setItemImgDtoList(itemImgDtoList);
//
//        return List<itemFormDto>list;
//    }

    public List<ItemOrderDto> getItemOrderDto(List<Long>ItemIds,List<Integer> counts, List<ItemSize> itemSizes){
        List<ItemOrderDto> itemOrderDtoList = new ArrayList<>();

        for(int i = 0; i<ItemIds.size(); i++){
            Item item = itemRepository.findById(ItemIds.get(i)).orElseThrow(() -> new EntityNotFoundException("Item not found"));
            ItemImg img = itemImgRepository.findByItemIdAndRepimgYn(ItemIds.get(i), "Y");

            ItemOrderDto itemOrderDto = new ItemOrderDto();
            itemOrderDto.setId(item.getId());
            itemOrderDto.setImgUrl(img.getOriUrl());
            itemOrderDto.setItemNm(item.getItemNm());
            itemOrderDto.setPrice(item.getPrice());
            itemOrderDto.setItemSize(itemSizes.get(i));
            itemOrderDto.setCount(counts.get(i));





            itemOrderDtoList.add(itemOrderDto);
        }
        log.info("test"+itemOrderDtoList);
        return itemOrderDtoList;
    }

    //상품 수정
    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws  Exception{

        Item item = itemRepository.findById(itemFormDto.getId())
                .orElseThrow(() -> new EntityNotFoundException());   //예> item_id 10번 값 추출

        item.updateItem(itemFormDto);  //10번값 변경


        List<Long> itemImgIds = itemFormDto.getItemImgIds();

        //이미지 등록
        for(int i=0; i<itemImgIds.size(); i++){
            log.info("itemImgIds.get(i) : " + itemImgIds.get(i));
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
        }
        return item.getId();
    }

    //검색 조건 & 페이징 처리
    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return  itemRepository.getAdiminItemPage(itemSearchDto, pageable);
    }

    //메인 페이지
    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        log.info("ItemSearchDto"+  itemSearchDto );
        return itemRepository.getMainItemPage(itemSearchDto, pageable);
    }


}
