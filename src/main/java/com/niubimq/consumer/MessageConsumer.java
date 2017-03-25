package com.niubimq.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

/**
 * <p>Description:消息消费者</p>
 * @author junjin4838
 * @date 2017年3月25日
 * @version 1.0
 */
public class MessageConsumer implements MessageListener {
	
	private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);


	@Override
	public void onMessage(Message message) {
		logger.info("receive message:{}",message);
		System.out.println(new String(message.getBody()));
	}

}
