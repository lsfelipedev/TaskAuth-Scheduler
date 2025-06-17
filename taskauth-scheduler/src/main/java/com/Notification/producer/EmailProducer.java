package com.Notification.producer;

import com.Notification.domain.Notification;
import com.Notification.dtos.notification.NotificationMsgDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmailProducer {

    private static final Logger log = LoggerFactory.getLogger(EmailProducer.class);

    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.queue.email.name}")
    private String queueName;

    public EmailProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishMessageEmail(Notification notification) {

        NotificationMsgDto dto = new NotificationMsgDto(
                notification.getUserSender().getId(),
                notification.getUserSender().getEmail(),
                notification.getUserDestination().getEmail(),
                notification.getMessage());

        log.info("Sending Communication request for the details {}", dto);
        rabbitTemplate.convertAndSend(queueName, dto);
    }
}
