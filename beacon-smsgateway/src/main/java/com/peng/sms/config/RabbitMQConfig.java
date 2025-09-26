package com.peng.sms.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.peng.sms.constant.RabbitMQConstants.*;


@Configuration
public class RabbitMQConfig {

    private final int TTL = 10000;
    private final String FANOUT_ROUTING_KEY = "";

    //  Declare a dead-letter queue: need to prepare a normal exchange, normal queue, dead-letter exchange, and dead-letter queue
//    @Bean
//    public Exchange normalExchange() {
//        return ExchangeBuilder.fanoutExchange(SMS_GATEWAY_NORMAL_EXCHANGE).build();
//    }
//
//    @Bean
//    public Queue normalQueue() {
//        Queue queue = QueueBuilder.durable(SMS_GATEWAY_NORMAL_QUEUE)
//                .withArgument("x-message-ttl", TTL)
//                .withArgument("x-dead-letter-exchange", SMS_GATEWAY_DEAD_EXCHANGE)
//                .withArgument("x-dead-letter-routing-key", FANOUT_ROUTING_KEY)
//                .build();
//        return queue;
//    }
//
//    @Bean
//    public Binding normalBinding(Exchange normalExchange, Queue normalQueue) {
//        return BindingBuilder.bind(normalQueue).to(normalExchange).with("").noargs();
//    }
//
//    @Bean
//    public Exchange deadExchange() {
//        return ExchangeBuilder.fanoutExchange(SMS_GATEWAY_DEAD_EXCHANGE).build();
//    }
//
//    @Bean
//    public Queue deadQueue() {
//        return QueueBuilder.durable(SMS_GATEWAY_DEAD_QUEUE).build();
//    }
//
//    @Bean
//    public Binding deadBinding(Exchange deadExchange, Queue deadQueue) {
//        return BindingBuilder.bind(deadQueue).to(deadExchange).with("").noargs();
//    }


    // another Configuration ways can be used to config the rabbitMQ, the setConcurrentConsumers and setPrefetchCount
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory,
                                                                               SimpleRabbitListenerContainerFactoryConfigurer configurer) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConcurrentConsumers(10);
        factory.setPrefetchCount(50);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        configurer.configure(factory, connectionFactory);
        return factory;
    }
}
