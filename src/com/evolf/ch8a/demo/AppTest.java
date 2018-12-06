package com.evolf.ch8a.demo;

import com.evolf.ch8a.PendingJobPool;
import com.evolf.ch8a.vo.TaskResult;
import com.evolf.tools.SleepTools;

import java.util.List;
import java.util.Random;

/**
 *@author Mark老师   itheone itheone
 *
 *类说明：模拟一个应用程序，提交工作和任务，并查询任务进度
 */
public class AppTest {
	
	private final static String JOB_NAME = "计算数值";
	private final static int JOB_LENGTH = 1000;
	
	//查询任务进度的线程
	private static class QueryResult implements Runnable{
		
		private PendingJobPool pool;

		public QueryResult(PendingJobPool pool) {
			super();
			this.pool = pool;
		}

		@Override
		public void run() {
			int i=0;//查询次数
			while(i<350) {
				List<TaskResult<String>> taskDetail = pool.getTaskDetail(JOB_NAME);
				if(!taskDetail.isEmpty()) {
					System.out.println(pool.getTaskProgess(JOB_NAME));
					System.out.println(taskDetail);					
				}
				SleepTools.ms(100);
				i++;
			}
		}
		
	}

	public static void main(String[] args) {
		MyTask myTask = new MyTask();
		//拿到框架的实例
		PendingJobPool pool = PendingJobPool.getInstance();
		//注册job + 任务处理器myTask（即预定义工作,在pool中进行job初始化，对外封装具体过程，初始化处理器等）（对外只提供了pool，job在内部实现）
		pool.registerJob(JOB_NAME, JOB_LENGTH, myTask,1000*5);
		Random r = new Random();
		for(int i=0;i<JOB_LENGTH;i++) {
			//依次推入Task （推入指定job，具体task）
			pool.putTask(JOB_NAME, r.nextInt(1000));
		}
		Thread t = new Thread(new QueryResult(pool));
		t.start();
	}
}
