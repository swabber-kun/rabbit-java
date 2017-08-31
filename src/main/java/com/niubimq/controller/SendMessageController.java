package com.niubimq.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.niubimq.pojo.DetailRes;
import com.niubimq.service.SendMessage;

/**
 * <p>
 * Description:
 * </p>
 * 
 * @author junjin4838
 * @date 2017年3月22日
 * @version 1.0
 */
@Controller
@RequestMapping("/rabbit")
public class SendMessageController {

	private static final Logger logger = LoggerFactory.getLogger(SendMessageController.class);

	@Autowired
	private SendMessage sendMessage;

	/**
	 * for test
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/send", method = RequestMethod.GET)
	@ResponseBody
	public DetailRes sendMessage() throws Exception {

		String exchange = "exchangeTest";

		String routing = "queueTestKey";

		String queue = "queueTest";

		String type = "direct";
		
		JSONObject json = new JSONObject();
		json.put("trackId", "jbk001");
		json.put("name", "jbk");
		json.put("url", "www.baidu.com");
		
		logger.info("发送的数据：" + json);

		DetailRes detailRes = sendMessage.send(exchange, routing, queue, type,json);

		return detailRes;

	}

}
