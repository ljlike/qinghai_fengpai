package com.daoyintech.daoyin_release.messageQueue.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * Created by pei
 */

@ConfigurationProperties(prefix = "queues")
@Data
public class QueueProperties {

    private String expireAndDeleteOrderDelay;

    private String orderLastOneHourDelay;

    private String orderDelay;

}
