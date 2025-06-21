package com.example.account_service.service;

import com.example.account_service.model.AccountCreation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSender {
    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Autowired
    private Queue queue;
    private static final Logger logger = LogManager.getLogger(RabbitMQSender.class.toString());
    public void send(AccountCreation accountCreation) {
        rabbitTemplate.convertAndSend(queue.getName(), accountCreation);
        logger.info("Sending Message to the Queue : " + accountCreation);
    }
}