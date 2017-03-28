package com.niubimq.service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.niubimq.pojo.DetailRes;

/**
 * <p>Description: </p>
 * @author junjin4838
 * @date 2017年3月25日
 * @version 1.0
 */
public interface ConsumerMessage {
	
	/**
	 * 消费消息
	 * @param exchange
	 * @param routingKey
	 * @param queue
	 * @param type
	 * @return
	 */
	DetailRes consumer(String exchange,String routingKey,String queue,String type) throws IOException, TimeoutException;

}
