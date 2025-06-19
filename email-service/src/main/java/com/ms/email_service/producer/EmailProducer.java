package com.ms.email_service.producer;

import com.ms.email_service.dto.NotificationMsgDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.exchange.main.name}")
    private String mainExchangeName;

    @Value("${spring.rabbitmq.routing-key.email.status}")
    private String statusRoutingKey;


    public void publishEmailStatus(NotificationMsgDto dto){

        rabbitTemplate.convertAndSend(mainExchangeName, statusRoutingKey, dto);
    }

}
