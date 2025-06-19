package com.ms.email_service.consumer;

import com.ms.email_service.dto.NotificationMsgDto;
import com.ms.email_service.producer.EmailProducer;
import com.ms.email_service.repository.EmailRepository;
import com.ms.email_service.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {

    private static final Logger log = LoggerFactory.getLogger(EmailConsumer.class);

    private final EmailService emailService;
    private final EmailProducer emailProducer;

    public EmailConsumer(EmailService emailService, EmailProducer emailProducer) {
        this.emailService = emailService;
        this.emailProducer = emailProducer;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.email.name}")
    public void sendNotificationEmail(NotificationMsgDto dto){

        log.info("Sending email with the details: {}", dto);
        emailService.sendEmail(dto);
        dto.setEmailSent(true);
        emailProducer.publishEmailStatus(dto);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.email.dlq.name}")
    public void dlqListener(NotificationMsgDto dto){

        log.error("Mensagem recebida na DLQ: {}", dto);
        emailProducer.publishEmailStatus(dto);

    }
}
