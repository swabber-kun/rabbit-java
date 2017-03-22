package com.niubimq.client;

import com.niubimq.cache.RetryCache;
import com.niubimq.pojo.Contants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;

/**
 * Created by junjin4838 on 17/3/17.
 */
public class RabbitFactory {

    private static final Logger logger = LoggerFactory.getLogger(RabbitFactory.class);

    /**
     * 创建RabbitMQ的模板类
     *
     * @return
     */
    public static RabbitTemplate createRabbitTemplate(RabbitTemplate rabbitTemplate,String exchange,String routingKey,RetryCache retryCache){

        //要求MQ返回数据
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setExchange(exchange);
        rabbitTemplate.setRoutingKey(routingKey);

        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {

                System.out.println("================");
                System.out.println("correlationData = " + correlationData);
                System.out.println("ack = " + ack);
                System.out.println("cause = " + cause);
                System.out.println("================");

                if(!ack){
                    logger.info("send message failed: " + cause + correlationData.toString());
                }else{
                    // 从缓存中删除
                    retryCache.del(correlationData.getId());
                }
            }
        });


        /**
         * 回调函数
         * 如果消息已经正确到达交换机，但是后续出错了，就会回调这个函数，并且会将这些数据返回给我们
         */
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {

                System.out.println("================");
                System.out.println("message = " + message);
                System.out.println("replyCode = " + replyCode);
                System.out.println("replyText = " + replyText);
                System.out.println("exchange = " + exchange);
                System.out.println("routingKey = " + routingKey);
                System.out.println("================");

                try {
                    Thread.sleep(Contants.ONE_SECOND);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                logger.info("send message failed: " + replyCode + " " + replyText);

                rabbitTemplate.send(message);

            }
        });

        return rabbitTemplate;

    }

}
