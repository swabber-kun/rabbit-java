package com.niubimq.service;

import com.rabbitmq.client.Channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.Connection;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Created by junjin4838 on 17/3/16.
 */
public abstract class MessageBaseService {

    protected static final Logger logger = LoggerFactory.getLogger(MessageBaseService.class);

    /**
     * 创建队列（发送消息，消费消息都需要执行创建队列）
     * 提高性能，使用非事务的方式创建队列
     * @param exchange
     * @param routingKey
     * @param queue
     * @param connection
     * @param type
     */
    public void buildQueue(String exchange, String routingKey, String queue, Connection connection, String type) throws IOException, TimeoutException {

        //false -- 使用非事务
        Channel channel = connection.createChannel(false);

        /**
         * 声明转发器
         * @params exchange 转发器
         * @params type 发送类型
         * @params durable 是否持久化（true,在服务重启的时候也会存活）
         * @params autoDelete 是否自动删除
         * @params Map<String, Object> arguments 参数
         */
        channel.exchangeDeclare(exchange, type, true, false, null);

        /**
         * 声明队列
         * @params queue 队列名称
         * @params durable 是否持久化（true,在服务重启的时候也会存活）
         * @params exclusive 是否私有的
         * @params autoDelete 是否自动删除
         * @params Map<String, Object> arguments 参数
         */
        channel.queueDeclare(queue, true, false, false, null);

        /**
         * 队列绑定到转发器
         */
        channel.queueBind(queue, exchange, routingKey);

        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("close channel exception " + e);
        } catch (TimeoutException e) {
            e.printStackTrace();
            logger.info("close channel time out " + e);
        }
    }

}
