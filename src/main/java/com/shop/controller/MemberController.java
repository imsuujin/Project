package com.shop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shop.constant.Role;
import com.shop.dto.MailDto;
import com.shop.dto.MemberFormDto;
import com.shop.dto.MemberUpdateDto;
import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import com.shop.service.MailService;
import com.shop.service.MemberService;
import com.shop.service.oAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Controller
@Log4j2
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final oAuthService oAuthService;
    private final MemberRepository memberRepository;
    private final MailService mailService;


    @GetMapping("/members/agree")
    public String checkAgree(){
        return "member/checkAgree";
    }

    @GetMapping("/members/new")
    public String newMember(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());

        return "member/memberForm";
    }

    //PRG방식
    @PostMapping("/members/new")
    public String memberForm(@Valid MemberFormDto memberFormDto,
                             BindingResult bindingResult , Model model) {

        log.info("memberFormDto::" + memberFormDto);
        if(bindingResult.hasErrors()) {
            return "member/memberForm";
        }

        try {
            //MemberFormDto >>> Member Entity 변환
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            //member 객체 저장(회원저장)
            memberService.savaMember(member);
        }catch (IllegalStateException e){  //중복회원 입력시 예외 발생
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }

        return "redirect:/";
    }

    @GetMapping("/members/login")
    public String loginMember(){
        return "member/memberLoginForm";
    }

    @GetMapping("/members/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요.");
        return "member/memberLoginForm";
    }

    @GetMapping(value="/oauth/kakao")
    public String kakaoConnect() {

        String redirect_uri = "http://localhost:8181/oauth/kakao/callback";

        log.info("카카오로 로그인 작동");
        StringBuffer url = new StringBuffer();
        url.append("https://kauth.kakao.com/oauth/authorize?");
        url.append("client_id=99d7689ed042fc6f9df444ad8dd8214c");
        url.append("&redirect_uri=" + redirect_uri);
        url.append("&response_type=code");
        return "redirect:" + url.toString();
    }

    @GetMapping("/oauth/kakao/callback")
    public String kakaoCallback(String code, HttpServletRequest request) {
        log.info("콜백작동...");
        log.info("code:" + code);

        // STEP2: 인가코드를 기반으로 토큰(Access Token) 발급
        String accessToken;
        try {
            accessToken = oAuthService.getAccessToken(code);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // STEP3: 토큰를 통해 사용자 정보 조회
        Member member;
        try {
            member = oAuthService.getKakaoInfo(accessToken);
            log.info("member값! :" + member);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // STEP4: 카카오 사용자 정보 확인
        Member member1 = oAuthService.ifNeedKakaoInfo(member);
        log.info("member1값!" + member1);

        // STEP5: 강제 로그인
        if (member1 != null) {
            HttpSession session = request.getSession(true);

            // 세션에 회원 정보 저장
            session.setAttribute("loginMember", member1);

            // OAuth2User 객체 생성
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("email", member1.getEmail());

          String role =  member1.getRole() == Role.USER ? "ROLE_USER" : "ROLE_ADMIN";

            OAuth2User oAuth2User = new DefaultOAuth2User(
                    Arrays.asList(new SimpleGrantedAuthority(role)),
                    attributes,
                    "email");

            // OAuth2AuthenticationToken 생성
            Authentication auth = new OAuth2AuthenticationToken(oAuth2User, oAuth2User.getAuthorities(), "kakao");

            // SecurityContext에 Authentication 객체 설정
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(auth);

            // 세션에 SecurityContext 저장
            session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);

            // 세션 타임아웃 설정 (1800초 == 30분)
            session.setMaxInactiveInterval(60 * 30);

            // 로그아웃 시 사용할 카카오토큰 추가
            session.setAttribute("kakaoToken", accessToken);

            log.info("User authenticated and session created: " + session.getId());
        }

        return "redirect:/";
    }
    @GetMapping("/kakao/logout")
    public String kakaoLogout(HttpSession session) {
        String accessToken = (String) session.getAttribute("kakaoToken");

        if(accessToken != null && !"".equals(accessToken)){
            try {
                oAuthService.kakaoDisconnect(accessToken);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            session.removeAttribute("kakaoToken");
            session.removeAttribute("loginMember");
        }else{
            System.out.println("accessToken is null");
        }

        return "redirect:/";
    }

    @GetMapping("/members/loginInfo")
    public String memberInfo(Principal principal, ModelMap modelMap){
        String loginId = principal.getName();
        Member member = memberRepository.findByEmail(loginId);
        modelMap.addAttribute("member", member);

        return "mypage/myInfo";
    }

    @GetMapping("/members/checkPwdForm")
    public String checkPwdView() {
        return "member/passwordCheckForm";
    }

    @GetMapping("/members/checkPwd")
    @ResponseBody
    public boolean checkPwdView(Principal principal,
                                Member member, @RequestParam String checkPassword,
                                Model model){

        String loginId = principal.getName();
        Member memberId = memberRepository.findByEmail(loginId);
        System.out.println(memberId.getPassword());
        return memberService.checkPassword(memberId, checkPassword);
    }

//    @GetMapping(value = "/updateForm")
//    public String updateMemberForm(Principal principal,Model model){
//        String loginId = principal.getName();
//        Member memberId = memberRepository.findByEmail(loginId);
//        model.addAttribute("member", memberId);
//
//        return "settings/memberUpdateForm";
//    }

    @GetMapping(value = "/members/pwd")
    public String pwdUpdateForm(Principal principal, Model model){
        String loginId = principal.getName();
        Member member = memberRepository.findByEmail(loginId);

        MemberUpdateDto memberUpdateDto = new MemberUpdateDto();

        memberUpdateDto.setPassword(member.getPassword());

        model.addAttribute("memberUpdateDto", memberUpdateDto);

        return "settings/pwdUpdateForm";
    }

    @PostMapping(value = "/members/pwd")
    public String updatePwd(@ModelAttribute MemberUpdateDto memberUpdateDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("memberUpdateDto", memberUpdateDto);
            return "settings/pwdUpdateForm";
        }

        try {
            memberService.updatePassword(memberUpdateDto);
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "비밀번호 변경 중 오류가 발생했습니다: " + e.getMessage());
            return "settings/pwdUpdateForm";
        }
    }

    @GetMapping(value = "/members/updateForm")
    public String updateMemberForm(Principal principal, Model model) {
        String loginId = principal.getName();
        Member member = memberRepository.findByEmail(loginId);

        MemberUpdateDto memberUpdateDto = new MemberUpdateDto();
        memberUpdateDto.setEmail(member.getEmail());
        memberUpdateDto.setName(member.getName());
        memberUpdateDto.setPhone(member.getPhone());
        memberUpdateDto.setAddress(member.getAddress());
        memberUpdateDto.setStreetAddress(member.getStreetAddress());
        memberUpdateDto.setDetailAddress(member.getDetailAddress());

        model.addAttribute("memberUpdateDto", memberUpdateDto);

        return "settings/memberUpdateForm";
    }


    @PostMapping(value = "/members/updateForm")
    public String updateMember(MemberUpdateDto memberUpdateDto, Model model){

        model.addAttribute("member", memberUpdateDto);
        memberService.updateMember(memberUpdateDto);
        return "redirect:/members/loginInfo";
    }

    @GetMapping(value = "/members/findMember")
    public String findMember(Model model){
        return "member/findMemberForm";
    }

    @Transactional
    @PostMapping("/sendEmail")
    public String sendEmail(@RequestParam("memberEmail") String memberEmail){
        MailDto dto = mailService.createMailAndChangePassword(memberEmail);
        mailService.mailSend(dto);

        return "member/memberLoginForm";
    }

    @RequestMapping(value = "/members/findId", method = RequestMethod.POST)
    @ResponseBody
    public String findId(@RequestParam("memberEmail") String memberEmail){
        String email = String.valueOf(memberRepository.findByEmail(memberEmail).getId());
        System.out.println("회원 이메일 " + email);
        if(email == null){
            return null;
        } else {
            return email;
        }

    }




}
