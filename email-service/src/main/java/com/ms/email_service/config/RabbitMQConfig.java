package com.ms.email_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.queue.email.name}")
    private String mainQueueName;

    @Value("${spring.rabbitmq.queue.email.dlq.name}")
    private String dlqQueueName;

    @Value("${spring.rabbitmq.exchange.dlx.name}")
    private String dlxExchangeName;

    @Value("${spring.rabbitmq.routing-key.dlq}")
    private String dlqRoutingKey;


    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        return new Jackson2JsonMessageConverter(objectMapper);
    }


    @Bean
    public Queue mainQueue(){
        return QueueBuilder
                .durable(mainQueueName)
                .withArgument("x-dead-letter-exchange", dlxExchangeName)
                .withArgument("x-dead-letter-routing-key", dlqRoutingKey)
                .build();
    }

    @Bean
    public Queue deadLetterQueue(){
        return QueueBuilder.durable(dlqQueueName).build();
    }

    @Bean
    public DirectExchange deadLetterExchange(){
        return new DirectExchange(dlxExchangeName);
    }

    @Bean
    public Binding dlqBinding(){
        return BindingBuilder
                .bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with(dlqRoutingKey);
    }
}
