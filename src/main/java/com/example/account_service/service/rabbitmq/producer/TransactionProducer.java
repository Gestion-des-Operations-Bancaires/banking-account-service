package com.example.account_service.service.rabbitmq.producer;

import com.example.account_service.event.TransactionEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TransactionProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.exchange.topic}")
    private String topicExchange;

    public void sendAccountCreationEvent(TransactionEvent event) {
        // Topic exchange routing
        rabbitTemplate.convertAndSend(
                topicExchange,
                "transaction.created",
                event
        );
    }
}