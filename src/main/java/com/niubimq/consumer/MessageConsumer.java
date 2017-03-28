package com.niubimq.consumer;

import java.io.UnsupportedEncodingException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

/**
 * <p>Description:使用监听的方式进行消息消费</p>
 * @author junjin4838
 * @date 2017年3月25日
 * @version 1.0
 */
public class MessageConsumer implements MessageListener {
	
	@Override
	public void onMessage(Message message) {

		//调用具体的业务逻辑
		try {
			ProcessMessage.process(message);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

}
