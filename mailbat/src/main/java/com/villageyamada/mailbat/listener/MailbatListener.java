package com.villageyamada.mailbat.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

public class MailbatListener extends JobExecutionListenerSupport {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void beforeJob(JobExecution jobExecution) {
		super.beforeJob(jobExecution);
		logger.info("ジョブ開始");
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		super.afterJob(jobExecution);
		logger.info("ジョブ終了");
	}
}
