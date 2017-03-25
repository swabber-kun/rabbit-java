package com.niubimq.product;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.niubimq.pojo.DetailRes;
import com.niubimq.service.SendMessage;

/**
 * <p>Description: 生产者测试用例</p>
 * @author junjin4838
 * @date 2017年3月25日
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(
	locations={
	    "classpath:spring/applicationContext.xml",    
	    "classpath:spring/spring-mvc.xml",
	    "classpath:spring/spring-rabbit.xml"  
	  } ) 
public class ProductMessageTest {
	
	@Autowired
	private SendMessage sendMessage;
	
	@Test
	public void test() throws Exception {
		
		String exchange = "github-exchange";

		String routing = "github-exchange";

		String queue = "queueTest";

		String type = "topic";

		String object = "github123";
		
		DetailRes detailRes = sendMessage.send(exchange, routing, queue, type,object);
		
		assertTrue("true", detailRes.getSuccess());
		
	}

}
