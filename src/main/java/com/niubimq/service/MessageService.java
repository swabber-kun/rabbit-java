package com.niubimq.service;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by junjin4838 on 17/3/16.
 */
public abstract class MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

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

        channel.exchangeDeclare(exchange, type, true, false, null);

        channel.queueDeclare(queue, true, false, false, null);

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
