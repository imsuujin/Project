package com.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
//현재 사용자의 정보를 반환하기 위한 클래스
public class AuditConfig {

    @Bean
    public AuditorAware<String> auditorProvider(){
        return new AuditorAwareImpl();
    }
}
