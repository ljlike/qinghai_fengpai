package com.daoyintech.daoyin_release.messageQueue.sender;

import com.daoyintech.daoyin_release.messageQueue.config.QueueProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@EnableConfigurationProperties(QueueProperties.class)
@Configuration
public class OrderSender {

    @Autowired
    private RabbitTemplate orderDelayTemplate;

    @Autowired
    private QueueProperties queueProperties;

    public void sendCheckOrderExpired(String orderNo) {
        orderDelayTemplate.convertAndSend(queueProperties.getOrderDelay(), orderNo);
    }
}
