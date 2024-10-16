//package com.shop.repository;
//
//import com.querydsl.core.BooleanBuilder;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import com.shop.constant.ItemSellStatus;
//import com.shop.entity.Item;
//import com.shop.entity.QItem;
//import lombok.extern.log4j.Log4j;
//import lombok.extern.log4j.Log4j2;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.test.annotation.Commit;
//import org.thymeleaf.util.StringUtils;
//
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.transaction.Transactional;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Log4j2
////@Transactional
//class ItemRepositoryTest {
//
//    @Autowired
//    ItemRepository itemRepository;
//
//    @PersistenceContext
////    @Autowired
//    EntityManager em;
//
//    @Test
//    @DisplayName("Querydsl 조회 테스트1")
//    public void queryDslTest(){
//
//        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
//
//        QItem qItem = QItem.item;
//
//        //select 조회 from 테이블명 where 조건
//
//         List<Item> item = queryFactory
//                 .selectFrom(qItem)
//                .where(qItem.itemNm.eq("한라산"))
//                 .orderBy(qItem.price.desc())
//                .fetch();
//
//         item.forEach(list->log.info(list));
//
//    }
//
//    @Test
//    @DisplayName("Querydsl 조회 테스트2")
//    public void queryDslTest2(){
//
//        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
//
//        QItem qItem = QItem.item;
//
//        //select 조회 from 테이블명 where 조건
//
//        Item item = queryFactory
//                .selectFrom(qItem)
//                .where( qItem.itemNm.eq("한라산").and(qItem.itemDetail.eq("상세1")) )
//                .fetchOne();
//
//        log.info(item);
//
//    }
//
//    @Test
//    @DisplayName("Querydsl price 20000만 이상 조회")
//    public void queryDslTest4() {
//        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
//        QItem qItem = QItem.item;
////        List<Item> item = queryFactory
////                .selectFrom(qItem)
////                .where(qItem.price.goe(20000))
////                .fetch();
////        log.info(item);
//
//        queryFactory
//                .selectFrom(qItem)
//                .where(qItem.price.goe(20000))
//                .fetch().forEach(list->log.info(list));
//    }
//
//    @Test
//    @DisplayName("상품 조회2")
//    public void queryDslTest5() {
////        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
//
//      //  createItemList2();
//
//        BooleanBuilder builder = new BooleanBuilder();
//
//        QItem item = QItem.item;
//
//        String itemDetail = "테스트";
//        int price = 10003;
//        String itmeSellStat = "SELL";
//
//        builder.and(item.itemDetail.like("%"+itemDetail+"%"));
//        builder.and(item.price.gt(price));
//        if(StringUtils.equals(itmeSellStat, ItemSellStatus.SELL)){
//            builder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
//        }
//
//        Pageable pageable = PageRequest.of(0,5);
//
//        Page<Item> itemPagingResult = itemRepository.findAll(builder, pageable);
//
//        log.info("------------------------------------------------");
//        log.info(itemPagingResult);
//        log.info("------------------------------------------------");
//
//        log.info("total elements : " + itemPagingResult.getTotalElements());
//        log.info("totla page : " + itemPagingResult.getTotalPages());
//
//        itemPagingResult.getContent().forEach(list->log.info(list));
//
//
//
//
//
//
//    }
//
//
////    @Commit
//    @Test
//    @DisplayName("상품 저장 테스트")
//    public void createItemTest(){
//        Item item = new Item();
//
//        item.setItemNm("라면");
//        item.setPrice(1500);
//        item.setItemDetail("라면 상세 설명2");
//        item.setItemSellStatus(ItemSellStatus.SELL);
//        item.setStockNumber(100);
//        item.setRegTime(LocalDateTime.now());
//        item.setUpdateTime(LocalDateTime.now());
//
//        Item savedItem = itemRepository.save(item);
//
//        log.info(savedItem);
//    }
//
//    @Test
//    @DisplayName("id번 검색")
//    public void findByIdTest(){
//        Optional<Item> result = itemRepository.findById(5L);
//
//        Item item = result.get();
//
//        log.info(item);
//    }
//
//    @Test
//    @DisplayName("라면 검색")
//    public void findByNameTest(){
//        Item item = itemRepository.findItemByItemNm("라면");
//
//        log.info("라면 : " + item);
//    }
//
//    @Test
//    @DisplayName("라면 상세 설명")
//    public void findByItemDetailTest(){
//       List<Item> item = itemRepository.findByItemDetail("상품");
//
//       item.forEach(list->log.info(list));
//
//
//    }
//
//    @Test
//    @DisplayName("라면 & 라면 상세 설명")
//    public void findByItemNmAndItemDetailTest(){
//       List<Item> item = itemRepository.findByItemNmAndItemDetail("라면","라면 상세 설명");
//
//        item.forEach(list->log.info(list));
//    }
//
//    @Test
//    @DisplayName("price가 30000이하 검색")
//    public void findByPriceTest(){
//        List<Item> list
//                = itemRepository.findByPriceLessThan(30000);
//
//        list.forEach(result-> log.info(result));
//    }
//
//
//    @Test
//    @DisplayName("findByItemDetailByNative")
//    public void findByItemDetailByNative(){
//        List<Item> items = itemRepository.findByItemDetailByNative("라면");
//        items.forEach(result-> log.info(result));
//    }
//
//
//    public void createItemList2(){
//        for(int i=1;i<=5;i++){
//            Item item = new Item();
//            item.setItemNm("테스트 상품" + i);
//            item.setPrice(10000 + i);
//            item.setItemDetail("테스트 상품 상세 설명" + i);
//            item.setItemSellStatus(ItemSellStatus.SELL);
//            item.setStockNumber(100);
//            item.setRegTime(LocalDateTime.now());
//            item.setUpdateTime(LocalDateTime.now());
//            itemRepository.save(item);
//        }
//
//        for(int i=6;i<=10;i++){
//            Item item = new Item();
//            item.setItemNm("테스트 상품" + i);
//            item.setPrice(10000 + i);
//            item.setItemDetail("테스트 상품 상세 설명" + i);
//            item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
//            item.setStockNumber(0);
//            item.setRegTime(LocalDateTime.now());
//            item.setUpdateTime(LocalDateTime.now());
//            itemRepository.save(item);
//        }
//    }
//
//
//
//
//
//}