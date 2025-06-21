package com.example.account_service.service.rabbitmq.listener;

import com.example.account_service.event.AccountEvent;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AccountListener {
    // Topic exchange listener (matches "account.*")
    @RabbitListener(bindings = @QueueBinding(
        value = @Queue("${spring.rabbitmq.queue.account}"),
        exchange = @Exchange(value = "${spring.rabbitmq.exchange.topic}", type = ExchangeTypes.TOPIC),
        key = "${spring.rabbitmq.routingkey.account}"
    ))
    public void handleAccountEvent(AccountEvent event) {
        System.out.println("Received account event: " + event);
    }

    // US-specific listener (matches "account.*.us")
   /* @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "us.account.queue", durable = "true"), // Auto-declare
            exchange = @Exchange(value = "topic.exchange", type = ExchangeTypes.TOPIC),
            key = "account.*.us"
    ))
    public void handleUSAccount(AccountEvent event) {
        System.out.println("US-specific account: " + event);
    }*/
}

