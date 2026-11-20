package com.guideme.ms3_reservations_payments.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "reservation.exchange";
    public static final String QUEUE_CONFIRMEE = "reservation.confirmee.queue";
    public static final String ROUTING_KEY_CONFIRMEE = "reservation.confirmee";

    @Bean
    public TopicExchange reservationExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue confirmeeQueue() {
        return new Queue(QUEUE_CONFIRMEE, true);
    }

    @Bean
    public Binding confirmeeBinding(Queue confirmeeQueue, TopicExchange reservationExchange) {
        return BindingBuilder.bind(confirmeeQueue).to(reservationExchange).with(ROUTING_KEY_CONFIRMEE);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }
}