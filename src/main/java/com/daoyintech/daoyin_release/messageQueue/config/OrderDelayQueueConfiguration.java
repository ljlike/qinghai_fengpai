package com.daoyintech.daoyin_release.messageQueue.config;

import com.daoyintech.daoyin_release.messageQueue.receiver.OrderHandlerReceiver;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(QueueProperties.class)
public class OrderDelayQueueConfiguration {

    @Autowired
    private QueueProperties properties;

    @Bean(name = "orderDelayQueue")
    Queue orderQueue() {

        return new Queue(properties.getOrderDelay(), false);
    }

    @Bean(name = "orderDelayExchange")
    CustomExchange orderDelayExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange("orderDelayExchange", "x-delayed-message", true, false, args);
    }

    @Bean(name = "orderBinding")
    Binding orderBinding(@Qualifier("orderDelayQueue") Queue queue, @Qualifier("orderDelayExchange") CustomExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(properties.getOrderDelay()).noargs();
    }

    @Bean(name = "orderContainer")
    SimpleMessageListenerContainer orderContainer(ConnectionFactory connectionFactory,
                                                  @Qualifier("orderListener") MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(properties.getOrderDelay());
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean(name = "orderListener")
    MessageListenerAdapter smsListenerAdapter(OrderHandlerReceiver receiver) {
        return new MessageListenerAdapter(receiver, "checkOrderExpired");
    }

    @Bean
    public RabbitTemplate orderDelayTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate();
        template.setExchange("orderDelayExchange");
        template.setConnectionFactory(connectionFactory);
        template.setBeforePublishPostProcessors((MessagePostProcessor) message -> {
            message.getMessageProperties().setHeader("x-delay", 1000 * 60 * 60);
            //    message.getMessageProperties().setHeader("x-delay", 1000 * 60 * 2);
            return message;
        });
        return template;
    }


}
