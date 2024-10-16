package com.shop.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration //@환경설정
@EnableWebSecurity //해당 클래스가 웹 애플리케이션의 Spring security를 구성하고 있음을 나타낸다.
@Log4j2
public class SecurityConfig { //

    @Bean //security 여러 설정들
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("--------------------securityFilterChain----------------------------");

        http.formLogin()
                        .loginPage("/members/login")
                        .defaultSuccessUrl("/")
                        .usernameParameter("email")  //로그인시 username으로 로그인 id일 때는 생략가능
                        .failureUrl("/members/login/error")
                .and()
                        .logout()
                        .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                        .logoutSuccessUrl("/")

                .and()
          .sessionManagement()
                .sessionFixation().newSession()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true);

        http.authorizeRequests()
                        .mvcMatchers("/css/**", "/js/**", "/img/**", "/oauth/**", "/mail/**", "/**", "/order/**","/settings/**").permitAll()
                        .mvcMatchers("/", "/members/**", "/item/**", "/images/**").permitAll()
                        .mvcMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated();

        http.exceptionHandling()
                        .authenticationEntryPoint(new CustomAuthticationEntryPoint());

        http.csrf().disable();

        return http.build();
    }

    @Bean //비밀번호 암호화
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
