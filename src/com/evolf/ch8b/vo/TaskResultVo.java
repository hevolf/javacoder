package com.evolf.ch8b.vo;

import java.util.concurrent.Future;


/**
 *@author Mark老师   itheone itheone
 *
 *类说明：生成题目时返回结果的定义
 */
public class TaskResultVo {
	
	private final String questionDetail;
	private final Future<QuestionInCacheVo> questionFuture;


	//只会返回题目文本（DB/Cache题目摘要一致）  或者  只返回future结果（不一致时）
	public TaskResultVo(String questionDetail) {
		this.questionDetail = questionDetail;
		this.questionFuture = null;
	}

	public TaskResultVo(Future<QuestionInCacheVo> questionFuture) {
		this.questionDetail = null;
		this.questionFuture = questionFuture;
	}
	
	public String getQuestionDetail() {
		return questionDetail;
	}
	public Future<QuestionInCacheVo> getQuestionFuture() {
		return questionFuture;
	}
	
	

}
