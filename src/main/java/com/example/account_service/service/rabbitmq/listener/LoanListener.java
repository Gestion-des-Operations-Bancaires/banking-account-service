package com.example.account_service.service.rabbitmq.listener;


import com.example.account_service.event.LoanEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RabbitListener(queues = "transaction.queue")
public class LoanListener {

    // Topic exchange listener (matches "account.*")
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("${spring.rabbitmq.queue.loan}"),
            exchange = @Exchange(value = "${spring.rabbitmq.exchange.topic}", type = ExchangeTypes.TOPIC),
            key = "${spring.rabbitmq.routingkey.loan}"
    ))
    public void handleAccountEvent(LoanEvent event) {
        log.info("Received account event: " + event);

    }
}