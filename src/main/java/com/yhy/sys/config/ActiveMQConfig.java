package com.yhy.sys.config;

import com.yhy.sys.domain.QueueEnum;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.jms.Queue;

@Configuration
public class ActiveMQConfig {

    @Bean
    public Queue queue() {
        return new ActiveMQQueue(QueueEnum.QUEUE_LOGINFO.getName());
    }
}
