package com.example.account_service.service;

import com.example.account_service.model.AccountCreation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
@Component
@RabbitListener(queues = "rabbitmq.queue", id = "listener")
public class RabbitMQReceiver {
    private static final Logger logger = LogManager.getLogger(RabbitMQReceiver.class.toString());
    @RabbitHandler
    public void receiver(AccountCreation accountCreation) {
        logger.info("AccountCreation listener invoked - Consuming Message with AccountCreation Identifier : " + accountCreation.toString());
    }
}