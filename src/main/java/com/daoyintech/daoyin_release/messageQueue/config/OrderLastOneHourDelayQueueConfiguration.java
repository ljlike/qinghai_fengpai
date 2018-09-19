package com.daoyintech.daoyin_release.messageQueue.config;

import com.daoyintech.daoyin_release.messageQueue.receiver.OrderLastOneHourHandlerReceiver;
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
public class OrderLastOneHourDelayQueueConfiguration {

    @Autowired
    private QueueProperties properties;

    @Bean(name = "orderLastOneHourDelayQueue")
    Queue orderLastOneHourQueue() {

        return new Queue(properties.getOrderLastOneHourDelay(), false);
    }

    @Bean(name = "orderLastOneHourDelayExchange")
    CustomExchange orderLastOneHourDelayExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange("orderLastOneHourDelayExchange", "x-delayed-message", true, false, args);
    }

    @Bean(name = "orderLastOneHourBinding")
    Binding orderLastOneHourBinding(@Qualifier("orderLastOneHourDelayQueue") Queue queue, @Qualifier("orderLastOneHourDelayExchange") CustomExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(properties.getOrderLastOneHourDelay()).noargs();
    }

    @Bean(name = "orderLastOneHourContainer")
    SimpleMessageListenerContainer orderLastOneHourContainer(ConnectionFactory connectionFactory,
                                                             @Qualifier("orderLastOneHourListener") MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(properties.getOrderLastOneHourDelay());
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean(name = "orderLastOneHourListener")
    MessageListenerAdapter smsListenerAdapter(OrderLastOneHourHandlerReceiver receiver) {
        return new MessageListenerAdapter(receiver, "checkOrderLastOneHourExpired");
    }

    @Bean
    public RabbitTemplate orderLastOneHourDelayTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate();
        template.setExchange("orderLastOneHourDelayExchange");
        template.setConnectionFactory(connectionFactory);
        template.setBeforePublishPostProcessors((MessagePostProcessor) message -> {
            message.getMessageProperties().setHeader("x-delay", 1000 * 60 * 30);//乘以2.5小时会出错
            //    message.getMessageProperties().setHeader("x-delay", 1000 * 60 * 2);
            return message;
        });
        return template;
    }


}
