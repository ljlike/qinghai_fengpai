package com.daoyintech.daoyin_release.messageQueue.config;

import com.daoyintech.daoyin_release.messageQueue.receiver.ExpireAndDeleteOrderHandlerReceiver;
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
public class ExpireAndDeleteOrderDelayQueueConfiguration {

    @Autowired
    private QueueProperties properties;

    @Bean(name = "expireAndDeleteOrderDelayQueue")
    Queue expireAndDeleteOrderQueue() {
        return new Queue(properties.getExpireAndDeleteOrderDelay(), false);
    }
    @Bean(name = "expireAndDeleteOrderDelayExchange")
    CustomExchange expireAndDeleteOrderDelayExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange("expireAndDeleteOrderDelayExchange", "x-delayed-message", true, false, args);
    }


    @Bean(name = "expireAndDeleteOrderBinding")
    Binding expireAndDeleteOrderBinding(@Qualifier("expireAndDeleteOrderDelayQueue") Queue queue, @Qualifier("expireAndDeleteOrderDelayExchange") CustomExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(properties.getExpireAndDeleteOrderDelay()).noargs();
    }

    @Bean(name = "expireAndDeleteOrderContainer")
    SimpleMessageListenerContainer expireAndDeleteOrderContainer(ConnectionFactory connectionFactory,
                                                          @Qualifier("expireAndDeleteOrderListener") MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(properties.getExpireAndDeleteOrderDelay());
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean(name = "expireAndDeleteOrderListener")
    MessageListenerAdapter eOrderListenerAdapter(ExpireAndDeleteOrderHandlerReceiver receiver) {
        return new MessageListenerAdapter(receiver, "checkOrderExpiredAndDelete");
    }

    @Bean
    public RabbitTemplate expireAndDeleteOrderDelayTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate();
        template.setExchange("expireAndDeleteOrderDelayExchange");
        template.setConnectionFactory(connectionFactory);
        template.setBeforePublishPostProcessors((MessagePostProcessor) message -> {
            message.getMessageProperties().setHeader("x-delay", 1000 * 60 * 60 * 24);
            //message.getMessageProperties().setHeader("x-delay", 1000 * 10);
            return message;
        });
        return template;
    }


}
