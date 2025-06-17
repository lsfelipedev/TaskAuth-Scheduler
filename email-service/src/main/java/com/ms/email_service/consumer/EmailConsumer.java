package com.ms.email_service.consumer;

import com.ms.email_service.dto.NotificationMsgDto;
import com.ms.email_service.mapper.EmailMapper;
import com.ms.email_service.model.Email;
import com.ms.email_service.model.EmailStatus;
import com.ms.email_service.repository.EmailRepository;
import com.ms.email_service.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EmailConsumer {

    private static final Logger log = LoggerFactory.getLogger(EmailConsumer.class);

    private final EmailService emailService;
    private final EmailRepository emailRepository;

    public EmailConsumer(EmailService emailService, EmailRepository emailRepository) {
        this.emailService = emailService;
        this.emailRepository = emailRepository;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.email.name}")
    public void sendNotificationEmail(NotificationMsgDto dto){

        log.info("Sending email with the details: {}", dto);
        emailService.sendEmail(dto);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.email.dlq.name}")
    public void dlqListener(NotificationMsgDto dto){

        log.error("Mensagem recebida na DLQ: {}", dto);

        Email email = EmailMapper.mapToEmail(dto, new Email());
        email.setSubject("Notification Register");
        email.setDateSent(LocalDateTime.now());
        email.setEmailStatus(EmailStatus.ERROR);
        emailRepository.save(email);

    }
}
