package com.shop.entity;

import com.shop.dto.MemberFormDto;
import com.shop.repository.CartRepository;
import com.shop.repository.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
//@Transactional
class CartTest {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    EntityManager em;


    public Member createMember() {
        MemberFormDto formDto = MemberFormDto.builder()
                .email("test3@email.com")
                .address("서울시 마포구 합정동")
                .password("1111")
                .build();

        return Member.createMember(formDto, passwordEncoder);
    }

    @Test
    @DisplayName("장바구니 회원 엔티티 매핑 조회 테스트")
    public  void findCartAndMemberTest(){
        Member member = createMember();

        memberRepository.save(member);

        Cart cart = new Cart();
        cart.setMember(member);
        cartRepository.save(cart);

     //   em.flush();   em.clear();

        Cart savedCart = cartRepository.findById(cart.getId()).orElseThrow(() -> new EntityNotFoundException());

        assertEquals(savedCart.getMember().getId(), member.getId());
    }

}














