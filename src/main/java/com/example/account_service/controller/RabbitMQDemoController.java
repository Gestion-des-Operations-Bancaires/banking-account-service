package com.example.account_service.controller;

import com.example.account_service.model.AccountCreation;
import com.example.account_service.service.RabbitMQSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping(value = "/rabbitmq")
public class RabbitMQDemoController {
    @Autowired
    RabbitMQSender rabbitMQSender;
    @PostMapping(value = "/sender")
    public String producer(@RequestBody AccountCreation accountCreation) {
        rabbitMQSender.send(accountCreation);
        return "Message sent to the RabbitMQ Queue Successfully";
    }
}