package com.shop.service;

import com.shop.dto.MailDto;
import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
@Log4j2
public class MailService {

    private final JavaMailSender javaMailSender;
    private static final String senderEmail = "hds9906@gmail.com";
    private static int number;
    private final MemberRepository memberRepository;

    private synchronized static void createNumber() {
        number = (int) (Math.random() * 900000) + 100000;
    }

    public MimeMessage createMail(String mail) {
        createNumber();
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("이메일 인증");
            String body = "<h3>요청하신 인증 번호입니다.</h3>" +
                    "<h1>" + number + "</h1>" +
                    "<h3>감사합니다.</h3>";
            message.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
            // 예외를 클라이언트에게 전달할 수 있도록 예외를 다시 던집니다.
            throw new RuntimeException("Failed to create email message", e);
        }
        return message;
    }

    public int sendMail(String email){
        MimeMessage message = createMail(email);
        javaMailSender.send(message);

        return number;
    }

    public static String getTempPassword(){
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        String str = "";

        int idx = 0;
        for(int i = 0; i < 10; i++){
            idx = (int)(charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;
    }

    public MailDto createMailAndChangePassword(String memberEmail){
        String str = getTempPassword();
        MailDto dto = new MailDto();
        dto.setAddress(memberEmail);
        dto.setTitle("임시비밀번호 안내 이메일 입니다.");
        dto.setMessage("안녕하세요.임시비밀번호 안내 관련 이메일 입니다." + " 회원님의 임시 비밀번호는 "
                + str + " 입니다." + "로그인 후에 비밀번호를 변경해주세요!");
        updatePassword(str, memberEmail);

        return dto;
    }

    public void mailSend(MailDto mailDto){
        System.out.println("전송 완료!");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDto.getAddress());
        message.setSubject(mailDto.getTitle());
        message.setText(mailDto.getMessage());
        message.setFrom("hds9906@gmail.com");
        message.setReplyTo("hds9906@gmail.com");
        System.out.println("message: " + message);
        javaMailSender.send(message);
    }

    public boolean updatePassword(String str, String email){
        try{
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String encodePw = encoder.encode(str);
            Member member = memberRepository.findByEmail(email);
            member.updatePassword(encodePw);
            memberRepository.save(member);
            log.info("str: " + str + " email: " + email);
            return true;

        } catch (Exception e){
            return false;
        }
    }
}
