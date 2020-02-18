package com.yhy.sys.config;

import com.yhy.sys.domain.QueueEnum;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.Queue;

@Configuration
public class ActiveMQConfig {

    @Bean
    public Queue queue() {
        return new ActiveMQQueue(QueueEnum.QUEUE_LOGINFO.getName());
    }
}
