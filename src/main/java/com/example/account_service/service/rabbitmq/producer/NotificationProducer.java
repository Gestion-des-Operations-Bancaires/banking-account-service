package com.example.account_service.service.rabbitmq.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.management.Notification;

@Service
public class NotificationProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.exchange.fanout}")
    private String fanoutExchange;

    public void broadcastNotification(Notification notification) {
        // Fanout exchange (no routing key needed)
        rabbitTemplate.convertAndSend(
            fanoutExchange,
            "", // routing key ignored for fanout
            notification
        );
    }
}
