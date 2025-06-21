package com.example.account_service.service.rabbitmq.listener;

import com.example.account_service.event.TransactionEvent;
import com.example.account_service.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RabbitListener(queues = "transaction.queue")
public class TransactionEventListener {
    private final AccountService accountService;

    public TransactionEventListener(AccountService accountService) {
        this.accountService = accountService;
    }

    // Topic exchange listener (matches "account.*")
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("${spring.rabbitmq.queue.transaction}"),
            exchange = @Exchange(value = "${spring.rabbitmq.exchange.topic}", type = ExchangeTypes.TOPIC),
            key = "${spring.rabbitmq.routingkey.transaction}"
    ))
    public void handleAccountEvent(TransactionEvent event) {
        log.info("Received account event: " + event);

        accountService.withdrawOrDepositMoneyOnAccount(event);
    }
}