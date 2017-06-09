package com.villageyamada.mailbat.tasklet;

import java.io.IOException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.mail.MailReceiver;
import org.springframework.stereotype.Component;

@Component
public class MailbatTasklet implements Tasklet {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MailReceiver receiver;

	@Override
	public RepeatStatus execute(StepContribution cont, ChunkContext chunk) {
		try {
			Message[] messages = (Message[]) receiver.receive();
			for (Message msg : messages) {
				if (msg != null) {
					if (!(msg.getContent() instanceof Multipart)) {
						logger.info(String.format("件名", msg.getSubject()));
					} else {
						logger.info("can not handle multipart mails. (ToT)/~~~");
					}
				}
			}
		} catch (MessagingException e) {
			logger.error("メール受信でエラーが発生。");
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error("メール本文の取得でエラーが発生。");
			logger.error(e.getMessage());
		}
		return RepeatStatus.FINISHED;
	}
}
