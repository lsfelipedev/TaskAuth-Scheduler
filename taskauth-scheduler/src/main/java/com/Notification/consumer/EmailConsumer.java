package com.Notification.consumer;

import com.Notification.dtos.notification.NotificationMsgDto;
import com.Notification.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailConsumer {

    private final NotificationService service;

    @RabbitListener(queues = "${spring.rabbitmq.queue.email.sent.name}")
    public void emailStatusCommunication(NotificationMsgDto dto){

        service.updateStatusNotification(dto);
    }
}
