package com.shop.controller;

import com.shop.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@Log4j2
public class MailController {

    private final MailService mailService;


    @ResponseBody
    @PostMapping("/mail")
    public String MailSend(String mail){
        log.info("작동");
        System.out.println("email : " + mail);
        int number = mailService.sendMail(mail);

        String num = "" + number;

        return num;
    }

}
