package com.shop.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Log4j2

//인증 확인 후 사용자 아이디 값 반환하는 클래스
public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String userId = "";

        //인증 확인
        if(authentication != null){
            //사용자의 Id값 변수에 넣음
            userId = authentication.getName();
        }

        //아이디 값 반환
        return Optional.of(userId);
    }
}
