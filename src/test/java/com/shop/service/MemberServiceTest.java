package com.shop.service;

import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
@SpringBootTest
@Log4j2
class MemberServiceTest {

    @Autowired
    MemberService  memberService;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private MemberRepository memberRepository;

    public Member createMember(){
        MemberFormDto memberFormDto = MemberFormDto.builder()
                .name("홍길동")
                .address("서울시 마포구 합정동")
                .email("test@test.com")
                .password("1234")
                .build();

        return Member.createMember(memberFormDto,passwordEncoder);
    }


    @Test
    @DisplayName("회원 가입 테스트")
    public void saveMemberTest(){
        Member member = createMember();

        Member savaedMember = memberService.savaMember(member);
    }

    @Test
    @DisplayName("중복 가입 테스트")
    public void saveDuplicateMemberTest(){
        Member member = createMember();
        Member member2 = createMember();

        Member savaedMember = memberService.savaMember(member);
        Member savaedMember2 = memberService.savaMember(member2);
    }


}