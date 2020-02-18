package com.yhy.sys.common;

import com.yhy.sys.domain.Loginfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Queue;

/**
 * 登陆日志消息发送者
 */
@Component
public class LoginfoSender {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    private Queue queue;

    public void sendMessage(Loginfo loginfo) {
        jmsMessagingTemplate.convertAndSend(queue, loginfo);

    }
}
