package com.niubimq.consumer;


import com.niubimq.client.RabbitFactory;
import com.niubimq.pojo.DetailRes;
import com.niubimq.service.ConsumerMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

/**
 * Created by junjin4838 on 17/3/27.
 */
public class ThreadPoolConsumer {

    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolConsumer.class);

    @Autowired
    private  ConsumerMessage consumerMessage;

    private ExecutorService executor;

    //线程停止位
    private volatile boolean stop = false;

    //线程数
    private int threadCount;

    //重试时间
    private long intervalMils;

    //转换器
    private String exchange;

    //路由键
    private String routingKey;

    //队列
    private String queue;

    //传播类型
    private String type;

    //消费者构造器
    private RabbitFactory rabbitFactory;

    public ThreadPoolConsumer(ExecutorService executor, int threadCount, long intervalMils, String exchange, String routingKey, String queue, String type, RabbitFactory rabbitFactory) {
        this.threadCount = threadCount;
        this.intervalMils = intervalMils;
        this.exchange = exchange;
        this.routingKey = routingKey;
        this.queue = queue;
        this.type = type;
        this.rabbitFactory = rabbitFactory;
        this.executor = Executors.newFixedThreadPool(threadCount);
    }

    /**
     * Step1: 构造messageConsumer
     * Step2: 执行Consume
     * @throws IOException
     */
    public void start() throws IOException{

        for (int i = 0; i < threadCount; i++) {

            executor.execute(new Runnable() {

                @Override
                public void run() {

                    while (!stop){

                        try {

                            DetailRes detailRes = consumerMessage.consumer(exchange,routingKey,queue,type);

                            if (intervalMils > 0) {
                                try {
                                    Thread.sleep(intervalMils);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                    logger.info("interrupt " + e);
                                }
                            }

                            if (!detailRes.getSuccess()) {
                                logger.info("run error " + detailRes.getErrMsg());
                            }


                        } catch (TimeoutException | IOException e ) {
                            e.printStackTrace();
                        }

                    }
                }
            });

        }
    }

    public void stop(){
        this.stop = true;
    }


}
