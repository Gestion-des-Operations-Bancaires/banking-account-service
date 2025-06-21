package com.example.account_service.service.rabbitmq.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.management.Notification;

@Component
public class NotificationListener {
    // Fanout exchange listener
    @RabbitListener(queues = "${spring.rabbitmq.queue.notification}")
    public void handleNotification(Notification notification) {
        System.out.println("Received notification: " + notification);
    }
}
