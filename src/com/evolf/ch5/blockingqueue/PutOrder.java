package com.evolf.ch5.blockingqueue;

import java.util.concurrent.DelayQueue;

/**
 *@author Mark老师   itheone itheone
 *
 *类说明：将订单放入队列
 */
public class PutOrder implements Runnable {
	
	private DelayQueue<ItemVo<Order>> queue;
	
	public PutOrder(DelayQueue<ItemVo<Order>> queue) {
		super();
		this.queue = queue;
	}

	@Override
	public void run() {
		
		//5秒到期  5秒后才能从队列取出（过期订单从队列移除）
		Order ordeTb = new Order("Tb12345",366);
		ItemVo<Order> itemTb = new ItemVo<Order>(5000,ordeTb);
		queue.offer(itemTb);
		System.out.println("订单5秒后到期："+ordeTb.getOrderNo());
		
		//8秒到期  8秒后才能从队列取出
		Order ordeJd = new Order("Jd54321",366);
		ItemVo<Order> itemJd = new ItemVo<Order>(8000,ordeJd);
		queue.offer(itemJd);
		System.out.println("订单8秒后到期："+ordeJd.getOrderNo());		
		
	}	
}
