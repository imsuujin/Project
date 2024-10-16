package com.shop.service;

import com.shop.dto.ItemOrderDto;
import com.shop.dto.OrderDto;
import com.shop.dto.OrderHistDto;
import com.shop.dto.OrderItemDto;
import com.shop.entity.*;
import com.shop.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final ItemImgRepository itemImgRepository;
    private final CartItemRepository cartItemRepository;

    public Long order(OrderDto orderDto, String email){
//        Optional<Item>results = itemRepository.findById(orderDto.getItemId());
//       Item item = results.orElseThrow(() -> new EntityNotFoundException());

        Item item = itemRepository.findById(orderDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);

        Member member = memberRepository.findByEmail(email);

        List<OrderItem> orderItemList  = new ArrayList<>();

        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount(), orderDto.getItemSize());

        orderItemList.add(orderItem);

        Order order = Order.createOrder(member, orderItemList);

        orderRepository.save(order);

        return order.getId();
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable){

        //주문 목록 조회(유저당)
        List<Order>orders = orderRepository.findOrders(email, pageable);
        Long totalCount = orderRepository.countOrder(email);

        List<OrderHistDto> orderHistDtos = new ArrayList<>();
        for(Order order : orders){
            OrderHistDto orderHistDto = new OrderHistDto(order);

            List<OrderItem> orderItems = order.getOrderItems(); //해당 주문의 주문 항목 리스트를 가져온다.

            for(OrderItem orderItem : orderItems){
                //4 주문 항목의 대표 이미지를 조회
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepimgYn(orderItem.getItem().getId(), "Y");
                //각 주문 항목에 대해 OrderItemDto 객체를 생성
                OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getOriUrl());
                //생성된 OrderItemDto를 OrderHistDto에 추가
                orderHistDto.addOrderItemDto(orderItemDto);
            }
            //OrderHistDto를 리스트에 추가
            orderHistDtos.add(orderHistDto);
        }
        return new PageImpl<OrderHistDto>(orderHistDtos, pageable, totalCount);


    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String email){
        Member curMember = memberRepository.findByEmail(email);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        Member savedMember = order.getMember();

        if(!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())){
            return false;
        }
        return true;
    }

    public void cancelOrder(Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        order.cancelOrder();
    }

    public Long orders(List<OrderDto> orderDtoList, String email){

        Member member = memberRepository.findByEmail(email);
        List<OrderItem> orderItemList = new ArrayList<>();

        for (OrderDto orderDto : orderDtoList) {
            Item item = itemRepository.findById(orderDto.getItemId())
                    .orElseThrow(EntityNotFoundException::new);

            OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount(),orderDto.getItemSize());
            orderItemList.add(orderItem);
        }

        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);

        return order.getId();
    }

    public List<ItemOrderDto> getCartItemDtos(List<Long> cartItemIds) {
        List<ItemOrderDto> ItemOrderDtos = new ArrayList<>();
        for (Long cartItemId : cartItemIds) {
            CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new EntityNotFoundException("CartItem not found"));
            Item item = itemRepository.findById(cartItem.getItem().getId()).orElseThrow(() -> new EntityNotFoundException("Item not found"));
            ItemImg itemImg = itemImgRepository.findByItemIdAndRepimgYn(cartItem.getItem().getId(), "Y");
            ItemOrderDto itemOrderDto = ItemOrderDto.builder()
                    .id(item.getId())
                    .itemSize(cartItem.getItemSize())
                    .count(cartItem.getCount())
                    .itemNm(item.getItemNm())
                    .price(item.getPrice())
                    .imgUrl(itemImg.getOriUrl())
                    .build();

            ItemOrderDtos.add(itemOrderDto);
        }
        log.info("ItemOrderDtos:" + ItemOrderDtos);
        return ItemOrderDtos;
    }
}
