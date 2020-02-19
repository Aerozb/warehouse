package com.yhy.sys.common;

import com.yhy.sys.domain.Loginfo;
import com.yhy.sys.service.LoginfoService;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;

/**
 * 登陆日志消息处理者
 */
@Component
public class LoginfoReceiver {

    @Autowired
    private LoginfoService loginfoService;

    @JmsListener(destination = "login-info", containerFactory = "myFactory")
    public void handle(Message message) {
        if (message instanceof ActiveMQObjectMessage) {
            ActiveMQObjectMessage activeMQObjectMessage = (ActiveMQObjectMessage) message;
            try {

                Loginfo loginfo = (Loginfo) activeMQObjectMessage.getObject();
                loginfoService.save(loginfo);
                //手动确认消息，才不会出现bug也被消费
                activeMQObjectMessage.acknowledge();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
