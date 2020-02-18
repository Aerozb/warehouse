package com.yhy.sys.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QueueEnum {

    /**
     * 消息通知队列
     */
    QUEUE_LOGINFO("login-info");

    private String name;
}
