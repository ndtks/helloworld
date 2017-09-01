package com.villageyamada.mailbat.tasklet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class MailReceiveTasklet implements Tasklet {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public RepeatStatus execute(StepContribution cont, ChunkContext chunk) {
		
		return RepeatStatus.FINISHED;
	}
}
