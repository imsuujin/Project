package com.shop.service;

import com.shop.dto.OrderDto;
import com.shop.entity.Item;
import com.shop.entity.Member;
import com.shop.entity.Order;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import javax.persistence.EntityNotFoundException;

@SpringBootTest
@Log4j2
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Commit
    public void order(){
        OrderDto orderDto = new OrderDto();
        orderDto.setItemId(2L);
        orderDto.setCount(1);

        String email ="test@test.com";

        Long orderId = orderService.order(orderDto, email);

        log.info("orderId:"+orderId);
    }

    @Test
    @DisplayName("주문 취소 테스트")
    public void cancelOrder(){
        Item item = new Item();
        item.setId(2L);
        Member member = new Member();
member.setEmail("test@naver.com")
;
        OrderDto orderDto = new OrderDto();

        orderDto.setCount(10);
        orderDto.setItemId(item.getId());
        Long orderId = orderService.order(orderDto, member.getEmail());

        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        orderService.cancelOrder(orderId);


    }
}
