package com.shop.controller;

import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.service.CartService;
import com.shop.service.ItemService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
@Log4j2
public class MainContoller {

    private final ItemService itemService;
    private final CartService cartService;

    public MainContoller(ItemService itemService, CartService cartService) {
        this.itemService = itemService;
        this.cartService = cartService;
    }

    @GetMapping("/")
    public String main(Principal principal,ItemSearchDto itemSearchDto, Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 8);

        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);

        log.info("-----------------main---------------------");
        log.info(items.getTotalElements());
        items.getContent().forEach(list-> log.info(list));

        int cartCount = cartService.getCartCount(principal);


        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);
        model.addAttribute("cartCount", cartCount);

        return "main";
    }
}
