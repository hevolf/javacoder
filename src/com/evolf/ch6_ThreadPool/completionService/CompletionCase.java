package com.evolf.ch6_ThreadPool.completionService;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *@author Mark老师   itheone itheone
 * CompletionService 任务先有结果的先入队列，所以输出结果是有序的
 *
 *类说明：
 */
public class CompletionCase {
    private final int POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private final int TOTAL_TASK = Runtime.getRuntime().availableProcessors();
//    private final int TOTAL_TASK = Runtime.getRuntime().availableProcessors()*10;

    // 方法一，自己写集合来实现获取线程池中任务的返回结果
    public void testByQueue() throws Exception {
    	long start = System.currentTimeMillis();
    	//统计所有任务休眠的总时长
    	AtomicInteger count = new AtomicInteger(0);
        // 创建线程池
        ExecutorService pool = Executors.newFixedThreadPool(POOL_SIZE);
        //容器存放提交给线程池的任务,list,map,  Linked有序
        BlockingQueue<Future<Integer>> queue = 
        		new LinkedBlockingQueue<Future<Integer>>();

        // 向里面扔任务
        for (int i = 0; i < TOTAL_TASK; i++) {
            //调用submit向 池中丢任务
            Future<Integer> future = pool.submit(new WorkTask("ExecTask" + i));
            queue.add(future);//i=0 先进队列，i=1的任务跟着进  （submit执行完后返回值加入到队列中保存）
        }

        // 检查线程池任务执行结果
        for (int i = 0; i < TOTAL_TASK; i++) {
        	int sleptTime = queue.take().get();///i=0先取到，i=1的后取到 从队列中获取任务返回结果
        	System.out.println(" slept "+sleptTime+" ms ...");        	
        	count.addAndGet(sleptTime);
        }

        // 关闭线程池
        pool.shutdown();
        System.out.println("-------------tasks sleep time "+count.get()
        		+"ms,and spend time "
        		+(System.currentTimeMillis()-start)+" ms");
    }

    // 方法二，通过CompletionService来实现获取线程池中任务的返回结果（先有结果的先返回）
    public void testByCompletion() throws Exception {
    	long start = System.currentTimeMillis();
    	AtomicInteger count = new AtomicInteger(0);
        // 创建线程池
        ExecutorService pool = Executors.newFixedThreadPool(POOL_SIZE);
        CompletionService<Integer> cService = new ExecutorCompletionService<>(pool);
        
        // 向里面扔任务 调用submit向 池中丢任务
        for (int i = 0; i < TOTAL_TASK; i++) {
        	cService.submit(new WorkTask("ExecTask" + i));
        }
        
        // 检查线程池任务执行结果
        for (int i = 0; i < TOTAL_TASK; i++) {
        	int sleptTime = cService.take().get();
        	System.out.println(" slept "+sleptTime+" ms ...");        	
        	count.addAndGet(sleptTime);
        }        

        // 关闭线程池
        pool.shutdown();
        System.out.println("-------------tasks sleep time "+count.get()
			+"ms,and spend time "
			+(System.currentTimeMillis()-start)+" ms");
    }

    public static void main(String[] args) throws Exception {
        CompletionCase t = new CompletionCase();
        t.testByQueue();
        t.testByCompletion();
    }
}
