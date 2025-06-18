package com.ms.email_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.exchange.main.name}")
    private String mainExchangeName;

    @Value("${spring.rabbitmq.queue.email.name}")
    private String mainQueueName;

    @Value("${spring.rabbitmq.routing-key.email.main}")
    private String mainRoutingKey;

    @Value("${spring.rabbitmq.queue.email.sent.name}")
    private String queueEmailSent;

    @Value("${spring.rabbitmq.routing-key.email.status}")
    private String statusRoutingKey;

    @Value("${spring.rabbitmq.queue.email.dlq.name}")
    private String dlqQueueName;

    @Value("${spring.rabbitmq.exchange.dlx.name}")
    private String dlxExchangeName;

    @Value("${spring.rabbitmq.routing-key.email.dlq}")
    private String dlqRoutingKey;


    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        return new Jackson2JsonMessageConverter(objectMapper);
    }


    @Bean
    public DirectExchange mainExchange(){
        return new DirectExchange(mainExchangeName);
    }

    @Bean
    public Queue queueEmailSent(){
        return QueueBuilder.durable(queueEmailSent).build();
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

    @Bean
    public Binding mainQueueBinding(){
        return BindingBuilder
                .bind(mainQueue())
                .to(mainExchange())
                .with(mainRoutingKey);
    }

    @Bean
    public Binding statusQueueBinding(){
        return BindingBuilder
                .bind(queueEmailSent())
                .to(mainExchange())
                .with(statusRoutingKey);
    }
}
