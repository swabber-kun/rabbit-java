package com.niubimq.service;

import com.niubimq.pojo.DetailRes;

/**
 * 处理消息体的业务逻辑
 * Created by junjin4838 on 17/3/28.
 */
public interface ProcessMessage {

    DetailRes process(Object message);

}
