package com.niubimq.service.impl;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.niubimq.service.ProcessMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.support.ConsumerCancelledException;
import org.springframework.amqp.rabbit.support.DefaultMessagePropertiesConverter;
import org.springframework.amqp.rabbit.support.MessagePropertiesConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;

import com.niubimq.client.RabbitFactory;
import com.niubimq.pojo.Contants;
import com.niubimq.pojo.DetailRes;
import com.niubimq.service.ConsumerMessage;
import com.niubimq.service.MessageBaseService;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * <p>Description: </p>
 * @author junjin4838
 * @date 2017年3月25日
 * @version 1.0
 */
public class ConsumerMessageImpl extends MessageBaseService implements ConsumerMessage{

    private static final Logger logger = LoggerFactory.getLogger(ConsumerMessageImpl.class);

    @Autowired
	private ConnectionFactory connectionFactory;

    @Autowired
    private ProcessMessage processMessage;

	/**
	 * Step1: 通过channel声明转换器，队列，绑定规则
	 * Step2: 设置Message序列号方法
	 * Step3: 构造consumer
	 */
	@Override
	public DetailRes consumer(String exchange, String routingKey, String queue,String type) throws IOException, TimeoutException {

        Connection connection = connectionFactory.createConnection();

        //Step1
        super.buildQueue(exchange, routingKey, queue, connection, type);

        //Step2
        final MessagePropertiesConverter messagePropertiesConverter = new DefaultMessagePropertiesConverter();
        final MessageConverter messageConverter = new Jackson2JsonMessageConverter();

        //Step3
        QueueingConsumer consumer = RabbitFactory.buildQueueConsumer(connection, queue);

        QueueingConsumer.Delivery delivery;

        Channel channel = consumer.getChannel();

        try {
            //1 通过delivery获取原始数据
            delivery = consumer.nextDelivery();
            Message message = new Message(delivery.getBody(),
                    messagePropertiesConverter.toMessageProperties(delivery.getProperties(), delivery.getEnvelope(), "UTF-8"));

            //2 将原始数据转换为特定类型的包
            Object messageBean = messageConverter.fromMessage(message);

            //3 处理数据
            DetailRes detailRes = processMessage.process(messageBean);

            //4  手动发送ack确认
            if (detailRes.getSuccess()) {
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            } else {
                //避免过多失败log
                Thread.sleep(Contants.ONE_SECOND);
                logger.info("process message failed: " + detailRes.getErrMsg());
                channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, true);
            }

            return detailRes;

        } catch (InterruptedException e) {

            e.printStackTrace();

            return new DetailRes(false, "interrupted exception " + e.toString());

        } catch (ShutdownSignalException | ConsumerCancelledException | IOException e) {

            e.printStackTrace();

            try {
                channel.close();
            } catch (IOException | TimeoutException ex) {
                ex.printStackTrace();
            }

            return new DetailRes(false, "shutdown or cancelled exception " + e.toString());
        }
	}

}
