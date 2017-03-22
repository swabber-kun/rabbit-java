package com.niubimq.cache;

import com.niubimq.pojo.Contants;
import com.niubimq.pojo.DetailRes;
import com.niubimq.pojo.MessageWithTime;
import com.niubimq.service.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 放置消息的缓存类
 * Created by junjin4838 on 17/3/15.
 */
public class RetryCache {

    private static final Logger logger = LoggerFactory.getLogger(RetryCache.class);

    private String exchange;

    private String routingKey;

    private String queue;

    private String type;

    @Autowired
    private  SendMessage sendMessage;

    /**
     * 放置缓存类
     */
    private Map<String, MessageWithTime> map = new ConcurrentHashMap<>();

    /**
     * 线程停止标志
     */
    private volatile boolean stop = false;

    /**
     * 计数器
     */
    private AtomicLong id = new AtomicLong();

    /**
     * 构造发送器
     * 这个时候就会有线程在尝试从缓存中获取失败数据然后重发
     */
    public void setSender(String exchange,String routingKey,String queue,String type,SendMessage sender) {
        this.exchange = exchange;
        this.routingKey = routingKey;
        this.queue = queue;
        this.type = type;
        startRetry();
    }

    /**
     * 开始执行重试
     */
    private void startRetry() {
        new Thread(new retryRunnable()).start();
    }

    /**
     * 添加数据到缓存类
     */
    public void add(String messageId, Object message) {
        map.put(messageId, new MessageWithTime(System.currentTimeMillis(), message));
    }

    /**
     * 删除消息ID
     */
    public void del(String id) {
        map.remove(id);
    }

    /**
     * 自增ID
     * @return
     */
    public String gengeateId() {
        return "" + id.incrementAndGet();
    }

    public class retryRunnable implements Runnable {

        @Override
        public void run() {

            //TODO#1
            while (stop) {

                try {
                    Thread.sleep(Contants.RETRY_TIME_INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                long now = System.currentTimeMillis();

                for (Map.Entry<String, MessageWithTime> entry : map.entrySet()) {

                    MessageWithTime messageWithTime = entry.getValue();

                    if (messageWithTime != null) {

                        if (messageWithTime.getTime() + 3 * Contants.VALID_TIME < now) {

                            //可以选择进DB进行实体记录
                            logger.info("send message failed after 3 min " + messageWithTime);

                        } else if (messageWithTime.getTime() + Contants.VALID_TIME < now) {

                            //还未到3分钟的消息
                            DetailRes detailRes = null;

                            try {
                                detailRes = sendMessage.send(exchange,routingKey,queue,type,messageWithTime.getMessage());
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (TimeoutException e) {
                                e.printStackTrace();
                            }

                            //判断是否发送成功,若成功，则在缓存中删除
                            if (detailRes != null && detailRes.getSuccess()) {
                                del(entry.getKey());
                            }

                        }
                    }
                }

            }
        }
    }


}
