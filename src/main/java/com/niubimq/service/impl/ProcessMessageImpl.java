package com.niubimq.service.impl;

import com.niubimq.pojo.DetailRes;
import com.niubimq.service.ProcessMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 处理消息里面的具体逻辑（我们业务需求是异步的调用URL）
 * Created by junjin4838 on 17/3/28.
 */
@Service
public class ProcessMessageImpl implements ProcessMessage {

    private static final Logger logger = LoggerFactory.getLogger(ProcessMessageImpl.class);

    @Override
    public DetailRes process(Object message) {

        DetailRes detailRes = null;

        try {
            logger.info("process message :" + message.toString());
            detailRes = new DetailRes(true,"处理成功");
            return detailRes;
        }catch (RuntimeException e){
            detailRes = new DetailRes(false,"处理失败");
            return detailRes;
        }
    }
}
