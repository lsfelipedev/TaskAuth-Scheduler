package com.ms.email_service.service;

import com.ms.email_service.dto.NotificationMsgDto;
import com.ms.email_service.mapper.EmailMapper;
import com.ms.email_service.model.Email;
import com.ms.email_service.repository.EmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailRepository emailRepository;
    private final JavaMailSender javaMailSender;

    @Value(value = "${spring.mail.username}")
    private String emailFrom;

    public Email sendEmail(NotificationMsgDto dto) throws MailException{

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(emailFrom);
            message.setTo(dto.getEmailTo());
            message.setSubject("Notification send by: " + dto.getEmailFrom());
            message.setText(dto.getMessage());

            javaMailSender.send(message);

        }
        catch (MailException e){
            throw new RuntimeException("Email service failed: ", e);
        }

        Email email = EmailMapper.mapToEmail(dto, new Email());

        email.setSubject("Notification Register");
        email.setDateSent(LocalDateTime.now());

        return emailRepository.save(email);
    }
}
