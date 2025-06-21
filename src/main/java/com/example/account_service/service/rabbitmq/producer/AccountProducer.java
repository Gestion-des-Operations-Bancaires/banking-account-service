package com.example.account_service.service.rabbitmq.producer;

import com.example.account_service.event.AccountEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class AccountProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.exchange.topic}")
    private String topicExchange;

    public void sendAccountCreated(AccountEvent event) {
        // Topic exchange routing
        rabbitTemplate.convertAndSend(
            topicExchange, 
            "account.created.us", 
            event
        );
    }
}

