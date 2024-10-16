package com.shop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.constant.Role;
import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
@Log4j2
public class oAuthService {

    @Autowired
    private MemberRepository memberRepository;

    public String getAccessToken(String code) throws JsonProcessingException {
        log.info("kakao auth");
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "99d7689ed042fc6f9df444ad8dd8214c");
        body.add("redirect_uri", "http://localhost:8181/oauth/kakao/callback");
        body.add("code", code);
        body.add("client_secret", "fDUVR435YyarIy3E2HxnI6ZHq4bP6klZ");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        return jsonNode.get("access_token").asText();
    }

    public Member getKakaoInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        // responseBody에 있는 정보 꺼내기
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        Long id = jsonNode.get("id").asLong();
        String email = jsonNode.get("kakao_account").get("email").asText();
        String name = jsonNode.get("properties")
                .get("nickname").asText();

        return new Member(id, name, email);
    }

    public Member ifNeedKakaoInfo (Member member) {
        log.info(member);
        log.info("KaKaoInfo 실행...");
        // DB에 중복되는 email이 있는지 확인
        String email = member.getEmail();
        Member Member = memberRepository.findByEmail(email);

        log.info("ifNeedKakaoInfo. Member 값 :" + Member);
        // 회원가입
        Member memberResult = null;
        if (Member == null) {
            String name = member.getName();

            // 임시 password 발급 - random UUID
            String tempPassword = UUID.randomUUID().toString();

            Member member1 = Member.builder()
                    .email(email)
                    .name(name)
                    .password(tempPassword)
                    .role(Role.USER)
                    .build();
            log.info("여기서 오류인가봅니다");
            memberRepository.save(member1);
            // DB 재조회
            memberResult = memberRepository.findByEmail(email);

            return memberResult;
        } else {
            return member;
        }
    }
    public void kakaoDisconnect(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoLogoutRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v1/user/logout",
                HttpMethod.POST,
                kakaoLogoutRequest,
                String.class
        );

        // responseBody에 있는 정보를 꺼냄
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        Long id = jsonNode.get("id").asLong();
        System.out.println("반환된 id: "+id);
    }

}
