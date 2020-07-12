package com.mateusz.jakuszko.roomforyou.scheduler;

import com.mateusz.jakuszko.roomforyou.dto.Mail;
import com.mateusz.jakuszko.roomforyou.entity.Reservation;
import com.mateusz.jakuszko.roomforyou.facade.searcher.SearcherDbFacade;
import com.mateusz.jakuszko.roomforyou.mapper.ReservationMapper;
import com.mateusz.jakuszko.roomforyou.service.EmailService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.List;

@Slf4j
@Component
@Data
@AllArgsConstructor
public class EmailScheduler {

    private EmailService emailService;
    private SearcherDbFacade searcherDbFacade;
    private ReservationMapper reservationMapper;
    private static final String RESERVATION_REMINDER_SUBJECT = "Room For You - reservation reminder";

    @Scheduled(cron = "0 0 12 * * 1")
    public void sendOncePerWeekMailAboutApplication() {
        log.info("Preparation to send mail");
        try {
            Mail mail = Mail.builder().mailTo("mateusz.jakuszko.kodilla@gmail.com").subject("RoomForYou")
                    .message("Come and reserve again some room for YOU!!").build();
            emailService.sendMail(mail, false);
        } catch (MessagingException e) {
            log.error("Error with sending mail - " + e.toString());
        }
        log.info("Email has been sent");
    }

    @Scheduled(cron = "0 0 12 * * 1")
    public void sendOnceADayMailForAllCustomersWhoHasReservationsEarlierThanInAWeek() {
        List<Reservation> reservations = searcherDbFacade.getReservationsIdThoseStartDateIsEarlierThanInAWeek();
        for (Reservation reservation : reservations) {
            log.info("Preparation to send mail");
            try {
                Mail mail = Mail.builder().mailTo(reservation.getCustomer().getEmail()).subject(RESERVATION_REMINDER_SUBJECT)
                        .message(reminderMessage(reservation)).build();
                emailService.sendMail(mail, false);
            } catch (MessagingException e) {
                log.error("Error with sending mail - " + e.toString());
            }
            log.info("Email has been sent");
        }
    }

    private String reminderMessage(Reservation reservation) {
        StringBuilder message = new StringBuilder();
        message.append("We want to remind You about your reservation in apartment which address is: ").append("\n")
                .append("City: ").append(reservation.getApartment().getCity()).append("\n")
                .append("Street: ").append(reservation.getApartment().getStreet()).append(" \n")
                .append("Street number: ").append(reservation.getApartment().getApartmentNumber()).append("\n")
                .append("The reservation starts in: ").append(reservation.getStartDate())
                .append(", and end in: ").append(reservation.getEndDate());
        return message.toString();
    }
}
