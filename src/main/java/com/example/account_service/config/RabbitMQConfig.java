package com.example.account_service.config;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.port}")
    private int portNumber;

    // Inject properties
    @Value("${spring.rabbitmq.exchange.direct}")
    private String directExchange;

    @Value("${spring.rabbitmq.exchange.fanout}")
    private String fanoutExchange;

    @Value("${spring.rabbitmq.exchange.topic}")
    private String topicExchange;

    @Value("${spring.rabbitmq.queue.account}")
    private String accountQueue;

    @Value("${spring.rabbitmq.queue.account}")
    private String loanQueue;

    @Value("${spring.rabbitmq.routingkey.account}")
    private String accountRoutingKey;

    @Value("${spring.rabbitmq.routingkey.transaction}")
    private String transactionRoutingKey;

    @Value("${spring.rabbitmq.routingkey.notification}")
    private String notificationRoutingKey;

    // Connection Factory
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(portNumber);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");
        return connectionFactory;
    }

    // Message Converter
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // RabbitTemplate for sending messages
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    // Listener Container Factory - REQUIRED for @RabbitListener
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        factory.setConcurrentConsumers(4);
        factory.setMaxConcurrentConsumers(10);
        factory.setDefaultRequeueRejected(false);
        return factory;
    }

    // Step 1: Exchanges
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(directExchange);
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(fanoutExchange);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(topicExchange);
    }

    // Step 2: Queues
    @Bean
    public Queue accountQueue() {
        return new Queue(accountQueue, true); // durable
    }

    @Bean
    public Queue loanQueue() {
        return new Queue(loanQueue, true); // durable
    }

    @Bean
    public Queue transactionQueue() {
        return new Queue("transaction.queue", true);
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue("notification.queue", true);
    }

    // Step 3: Bindings
    @Bean
    public Binding accountBinding() {
        return BindingBuilder.bind(accountQueue())
                .to(topicExchange())
                .with(accountRoutingKey);
    }

    // Fanout example (broadcast to all bound queues)
    @Bean
    public Binding fanoutBinding1() {
        return BindingBuilder.bind(accountQueue())
                .to(fanoutExchange());
    }

    @Bean
    public Binding fanoutBinding2() {
        return BindingBuilder.bind(notificationQueue())
                .to(fanoutExchange());
    }

    // Direct exchange example (1:1 routing)
    @Bean
    public Binding directBinding() {
        return BindingBuilder.bind(transactionQueue())
                .to(directExchange())
                .with("transaction.process");
    }
}