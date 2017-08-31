package com.niubimq.consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * <p>Description: </p>
 * @author junjin4838
 * @date 2017年4月6日
 * @version 1.0
 */
public class FutureDemo {
	
	private static long random() throws InterruptedException {
		Thread.sleep(1000);
		return new Random().nextInt(100);
	}
	
	/**
	 * 串行执行
	 * @return
	 * @throws InterruptedException 
	 */
	@SuppressWarnings("static-access")
	private static long serializableHandle() throws InterruptedException{
		
		List<Long> list = new ArrayList<Long>();
		
		FutureDemo futureDemo = new FutureDemo();
		
		long start = System.currentTimeMillis();
		
		list.add(futureDemo.random());
		list.add(futureDemo.random());
		list.add(futureDemo.random());
		list.add(futureDemo.random());
		
		int total = 0;
		
		for(int i=0;i<list.size();i++){
			total += list.get(i);
		}
		
		long end = System.currentTimeMillis();
		
		long useTime = end - start;
		
		return useTime;
	}
	
	/**
	 * 并行处理数据
	 * @return
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public static long synchronizedHandle() throws InterruptedException, ExecutionException {
		
		//任务列表
		List<FutureTask<Long>> futureTasks = new ArrayList<FutureTask<Long>>();  
		
		//创建线程池
		ExecutorService executor = Executors.newFixedThreadPool(10);
		
		long start = System.currentTimeMillis();
		
        Callable<Long> callable = new Callable<Long>(){

			@Override
			public Long call() throws Exception {
				Thread.sleep(1000);
				Long res = new Random().nextLong();
				return res;
			}		
        
        };
        
        for(int i=0;i<10;i++){
        	
        	FutureTask<Long> futureTask = new FutureTask<Long>(callable);
        	
        	//将处理的结果存放在缓存中
        	futureTasks.add(futureTask);
        	
        	//提交任务，这边是异步处理
        	executor.submit(futureTask);
        	
        }
        
        int count = 0 ;
        
        for(FutureTask<Long> futureTask : futureTasks){
        	count+= futureTask.get();  
        }
        
        long end = System.currentTimeMillis();  
        
        return end - start;
        
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		
		long time1 = serializableHandle();
		
		System.out.println("串行时间：" + time1);
		
		long time2 = synchronizedHandle();
		
		System.out.println("并行时间：" + time2);
		
	}
	
	
	
	

}
