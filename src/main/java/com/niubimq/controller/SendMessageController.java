package com.niubimq.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

		String exchange = "github-exchange";

		String routing = "github-exchange";

		String queue = "github-exchange";

		String type = "topic";

		String object = "github123";

		DetailRes detailRes = sendMessage.send(exchange, routing, queue, type,object);

		return detailRes;

	}

}
