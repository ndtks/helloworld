package com.villageyamada.mailbat.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.integration.mail.Pop3MailReceiver;

import com.villageyamada.mailbat.listener.MailbatListener;
import com.villageyamada.mailbat.tasklet.MailbatTasklet;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	@Autowired
	private Environment env;

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private MailbatTasklet mailBatTasklet;

	@Bean
	public Pop3MailReceiver pop3MailReceiver() {
		return new Pop3MailReceiver(
				env.getProperty("pop3.host"),
				env.getProperty("pop3.username"),
				env.getProperty("pop3.password"));
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
				.tasklet(mailBatTasklet)
				.build();
	}

	@Bean
	public Job job1(Step step1) {
		return jobBuilderFactory.get("job1")
				.incrementer(new RunIdIncrementer())
				.listener(listener())
				.start(step1)
				.build();
	}

	@Bean
	public JobExecutionListener listener() {
		return new MailbatListener();
	}
}
