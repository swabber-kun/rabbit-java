package com.niubimq.consumer;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.utils.SerializationUtils;

import com.alibaba.fastjson.JSONObject;
import com.niubimq.pojo.DetailRes;
import com.niubimq.util.FastJsonUtil;

/**
 * 处理消息里面的具体逻辑（我们业务需求是异步的调用URL） Created by junjin4838 on 17/3/28.
 */
public class ProcessMessage {

	private static final Logger logger = LoggerFactory.getLogger(ProcessMessage.class);

	private static final String ENCODING = Charset.defaultCharset().name();

	/**
	 * 处理数据的具体业务逻辑
	 * 
	 * Step1:获取Message
	 * Step2:异步调用URL，获取请求结果
	 * 
	 * @param message
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static DetailRes process(Message message) throws UnsupportedEncodingException {

		DetailRes detailRes = null;

		try {

			//Step1
			byte[] body = message.getBody();
			MessageProperties messageProperties = message.getMessageProperties();
			String messageStr = getBodyContentAsString(body,messageProperties);
			
			JSONObject messageObj = (JSONObject)FastJsonUtil.toJSON(messageStr);
			
			String url = (String) messageObj.get("url");
			
			//Step2
			

			detailRes = new DetailRes(true, "处理成功");
			return detailRes;
		} catch (RuntimeException e) {
			detailRes = new DetailRes(false, "处理失败");
			return detailRes;
		}
	}

	/**
	 * 根据不同的标签将字节码转成字符串
	 * @param body
	 * @param messageProperties
	 * @return
	 */
	private static String getBodyContentAsString(byte[] body,MessageProperties messageProperties) {
		
		if (body == null) {
			return null;
		}
		
		try {
			String contentType = (messageProperties != null) ? messageProperties.getContentType() : null;
			
			if (MessageProperties.CONTENT_TYPE_SERIALIZED_OBJECT.equals(contentType)) {
				return SerializationUtils.deserialize(body).toString();
			}
			
			if (MessageProperties.CONTENT_TYPE_TEXT_PLAIN.equals(contentType)
					|| MessageProperties.CONTENT_TYPE_JSON.equals(contentType)
					|| MessageProperties.CONTENT_TYPE_JSON_ALT.equals(contentType)
					|| MessageProperties.CONTENT_TYPE_XML.equals(contentType)) {
				
				return new String(body, ENCODING);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return body.toString();
	}
}
