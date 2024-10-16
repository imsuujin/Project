package com.shop.controller;

import com.shop.constant.ItemSize;
import com.shop.dto.*;
import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import com.shop.service.ItemService;
import com.shop.service.MemberService;
import com.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Log4j2
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final ItemService itemService;

    @PostMapping(value = "/order")
    public @ResponseBody ResponseEntity order(@RequestBody @Valid OrderDto orderDto,
                                              BindingResult bindingResult,
                                              Principal principal) {
        log.info("/order : OrderDto: " + orderDto);
        if(bindingResult.hasErrors()) {
            StringBuffer sb = new StringBuffer();

            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for(FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        String email = principal.getName();
        Long OrderId;

        try{
            OrderId = orderService.order(orderDto, email);
        }catch(Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);

        }
        return new ResponseEntity<Long>(OrderId, HttpStatus.OK);
    }

    @GetMapping (value = {"/orders", "/orders/{page}"})
    public String orderHist(@PathVariable ("page") Optional<Integer> page,
                            Principal principal, Model model){
        // 1 페이지 번호와 페이지 사이즈를 설정하여 페이지 요청 객체 생성
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4);



        // 2 주문 이력을 페이지별로 조회
        Page<OrderHistDto> OrderHistDtoList =
                orderService.getOrderList(principal.getName(), pageable);

        // 모델에 조회된 주문 이력과 페이지 정보 추가
        model.addAttribute("orders", OrderHistDtoList);
        model.addAttribute("page",pageable.getPageNumber());
        model.addAttribute("maxPage",5);

        return "order/orderHist"; // 주문 이력 페이지로 이동
    }

    @PostMapping("/order/{orderId}/cancel")
    public @ResponseBody ResponseEntity cancelOrder(
            @PathVariable("orderId")Long orderId, Principal principal){

        log.info("orderId:" + orderId);
        if(!orderService.validateOrder(orderId, principal.getName())){
            return new ResponseEntity<String>("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        orderService.cancelOrder(orderId);
        return new ResponseEntity<Long>(orderId,HttpStatus.OK);
    }

    @GetMapping("/order/cartOrderDtl")
    public String cartOrderDtl (@RequestParam("cartItemId")List<Long>cartItemId, Model model, Principal principal){
        log.info("cartItemId:Line100:" + cartItemId);
        List<ItemOrderDto> itemOrderDtoList = orderService.getCartItemDtos(cartItemId);
        Member member = memberRepository.findByEmail(principal.getName());
        log.info("orderItemDtoList:Line102:" + itemOrderDtoList);
        int allPrice = itemOrderDtoList.stream().mapToInt(dto -> dto.getPrice() * dto.getCount()).sum();
        log.info("member:" + member);

        log.info("allPrice:" + allPrice);
        model.addAttribute("items", itemOrderDtoList);
        model.addAttribute("member", member);
        model.addAttribute("allPrice", allPrice);
        return "order/orderDtl";
    }

    @GetMapping("/order/orderDtl")
    public String orderDtl(@RequestParam("itemId")List<Long>itemIds, @RequestParam("count")List<Integer>counts, @RequestParam("itemSize")List<ItemSize>itemSizes , Model model, Principal principal){
        log.info("작동 여부 확인");
        log.info("count:" + counts);
        log.info("it    emIds"+itemIds);
        List<ItemOrderDto>itemOrderDtoList = itemService.getItemOrderDto(itemIds, counts, itemSizes);
        for(ItemOrderDto itemOrderDto:itemOrderDtoList){
            log.info("itemOrderDto:" + itemOrderDto);

        }

        Member member = memberRepository.findByEmail(principal.getName());
        log.info("member" + member);
        log.info("아이템 오더리스트:"+ itemOrderDtoList);
        model.addAttribute("member", member);
        model.addAttribute("items", itemOrderDtoList);
        int allPrice = itemOrderDtoList.stream().mapToInt(dto -> dto.getPrice() * dto.getCount()).sum();
//        log.info("allPrice:" + allPrice);
        model.addAttribute("allPrice", allPrice);

        return "order/orderDtl";
    }
}
