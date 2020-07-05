package com.mateusz.jakuszko.roomforyou.scheduler;

import com.mateusz.jakuszko.roomforyou.dto.Mail;
import com.mateusz.jakuszko.roomforyou.service.EmailService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;

@Slf4j
@Component
@Data
@AllArgsConstructor
public class EmailScheduler {

    private EmailService emailService;

    @Scheduled(cron = "*/10 * * * * *")
    public void sendOnceADayMailAboutReservations() throws MessagingException {
        log.info("Preparation to send mail");
        try {
            Mail mail = Mail.builder().mailTo("mateusz.jakuszko.kodilla@gmail.com").subject("Powrót do domu").message("Dzień dobry, pozdrawiam").build();
            emailService.sendMail(mail, false);
        } catch (MessagingException e) {
            log.error("Error with sending mail - " + e.toString());
        }
        log.info("Email has been sent");
    }
}
