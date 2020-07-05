package com.mateusz.jakuszko.roomforyou;

import com.mateusz.jakuszko.roomforyou.dto.Mail;
import com.mateusz.jakuszko.roomforyou.service.EmailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class test {

    @Autowired
    private EmailService emailService;

    @Test
    public void test() throws MessagingException {
        Mail mail = Mail.builder().mailTo("mateusz.jakuszko.kodilla@gmail.com").subject("Powrót do domu").message("Dzień dobry, pozdrawiam").build();
        emailService.sendMail(mail, false);
    }
}
