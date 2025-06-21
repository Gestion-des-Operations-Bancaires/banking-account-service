package com.example.account_service.controller;

import com.example.account_service.event.AccountEvent;
import com.example.account_service.event.TransactionEvent;
import com.example.account_service.service.rabbitmq.producer.AccountProducer;
import com.example.account_service.service.rabbitmq.producer.TransactionProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping(value = "/rabbitmq")
public class RabbitMQDemoController {
    @Autowired
    TransactionProducer transactionProducer;
    @Autowired
    private AccountProducer accountProducer;

    @PostMapping(value = "/account_event")
    public String accountCreationEvent(@RequestBody AccountEvent accountEvent) {
        accountProducer.sendAccountCreated(accountEvent);
        return "Message sent to the RabbitMQ Queue Successfully";
    }

    @PostMapping(value = "/transaction_event")
    public String transactionEvent(@RequestBody TransactionEvent transactionEvent) {
        transactionProducer.sendAccountCreationEvent(transactionEvent);
        return "Message sent to the RabbitMQ Queue Successfully";
    }
}