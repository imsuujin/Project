package com.shop.entity;

import com.shop.constant.ItemSellStatus;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.OrderItemRepository;
import com.shop.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
@Transactional
class OrderTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Test
    @DisplayName("지연 로딩 테스트")
    public void lazyLoadindTest(){

        Order order = this.createOrder();

        Long orderItemId = order.getOrderItems().get(0).getId();

        em.flush();
        em.clear();

        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(EntityNotFoundException::new);
        log.info("Order class : " + orderItem.getOrder().getClass());

    }


    public Order createOrder(){
        Order order = new Order();

        for(int i=0; i<3; i++){
            Item item = this.createItem();
            itemRepository.save(item);

            OrderItem orderItem = new OrderItem();

            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(100);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }

        Member member = new Member();
        memberRepository.save(member);

        order.setMember(member);
        orderRepository.save(order);
        return order;
    }

    @Test
    @DisplayName("고아 객체 제거 테스트")
    public void orphanRemovealTest(){
        Order order = this.createOrder();

        order.getOrderItems().remove(0);
        em.flush();
    }

    public Item createItem(){
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("상세설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());

        return item;
    }

    @Test
    @DisplayName("영속성 전이 테스트")
    public void cacadeTest(){

        Order order = new Order();

        for(int i=0; i<3; i++){
            Item item = this.createItem();
            itemRepository.save(item);

            OrderItem orderItem = new OrderItem();

            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }

        orderRepository.saveAndFlush(order);
        em.clear();

        Order savedOrder = orderRepository.findById(order.getId())
                .orElseThrow(()->new EntityNotFoundException());

        log.info("======> " + savedOrder.getOrderItems().size());

        assertEquals(3, savedOrder.getOrderItems().size());
    }



}


















