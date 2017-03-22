package com.niubimq.service;

import com.niubimq.pojo.DetailRes;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 发送消息接口
 * Created by junjin4838 on 17/3/15.
 */
public interface SendMessage {

    /**
     * 发送消息
     * @param exchange 交换器
     * @param routingKey 路由
     * @param queue  队列
     * @param type 类型
     * @param message 消息体
     * @return
     */
    DetailRes send(String exchange, String routingKey, String queue, String type, Object message) throws IOException, TimeoutException;

}