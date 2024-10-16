package com.shop.service;

import com.shop.dto.MemberUpdateDto;
import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member savaMember(Member member) {
        validdateDuplicatemember(member);
        return memberRepository.save(member);
    }

    private void validdateDuplicatemember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());

        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("--------------------loadUserByUsername---------------------------------");
        log.info(email);
        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }

    private OAuth2User getOAuth2UserFromToken(String code) {
        // 토큰을 통해 OAuth2User 정보 가져오는 로직 (예: 카카오 API 호출)
        Map<String, Object> attributes = Collections.singletonMap("email", "user@example.com");
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                "email"
        );
    }

    @ResponseBody
    public boolean checkPassword(Member member, String checkPassword) {
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember == null) {
            throw new IllegalStateException("없는 회원입니다.");
        }
        String realPassword = member.getPassword();
        boolean matches = passwordEncoder.matches(checkPassword, realPassword);
        log.info("찍힙니까? 찍혔으면 : " + matches);
        return matches;
    }
    public void updatePassword(MemberUpdateDto memberUpdateDto) {
        if (memberUpdateDto == null || memberUpdateDto.getPassword() == null) {
            throw new IllegalArgumentException("MemberUpdateDto 또는 비밀번호가 null입니다.");
        }

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            throw new IllegalArgumentException("로그인된 사용자를 찾을 수 없습니다.");
        }

        member.setPassword(passwordEncoder.encode(memberUpdateDto.getPassword()));
        memberRepository.save(member);
    }

    public Long updateMember(MemberUpdateDto memberUpdateDto){
        Member member = memberRepository.findByEmail(memberUpdateDto.getEmail());
        member.updateUsername(memberUpdateDto.getName());
        member.updatePhone(memberUpdateDto.getPhone());
        member.updateAddress(memberUpdateDto.getAddress());
        member.updateStreetAddress(memberUpdateDto.getStreetAddress());
        member.updateDetailAddress(memberUpdateDto.getDetailAddress());



        memberRepository.save(member);

        return member.getId();
    }
}
