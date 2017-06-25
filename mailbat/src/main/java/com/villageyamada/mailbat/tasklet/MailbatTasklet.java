package com.villageyamada.mailbat.tasklet;

import java.io.IOException;
import java.util.Enumeration;

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
import org.springframework.integration.mail.Pop3MailReceiver;
import org.springframework.stereotype.Component;

import com.villageyamada.mailbat.dao.MailDao;

@Component
public class MailbatTasklet implements Tasklet {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private Pop3MailReceiver receiver;

	@Autowired
	private MailDao mailDao;

	@Override
	public RepeatStatus execute(StepContribution cont, ChunkContext chunk) {
		try {
//			ExpressionParser parser = new SpelExpressionParser();
//			Expression expression = parser.parseExpression("sentDate > new java.util.Date()");
//			receiver.setSelectorExpression(expression);
			Message[] messages = (Message[]) receiver.receive();
			Enumeration<Object> headers;
			for (Message msg : messages) {
				if (msg == null) {
					continue;
				}
				if (!(msg.getContent() instanceof Multipart)) {
					headers = msg.getAllHeaders();
					logger.info("-----");
					logger.info(String.format("件名: %s", msg.getSubject()));
					logger.info(String.format("送信元: %s", msg.getFrom()[0].toString()));
					logger.info(String.format("送信日: %s", msg.getSentDate()));
					logger.info(String.format("ContentType: %s", msg.getContentType()));
					logger.info(String.format("本文: %s", msg.getContent().toString().substring(0, 20)));
					mailDao.regMail(msg);
//					while (headers.hasMoreElements()) {
//						Header header = (Header)headers.nextElement();
//						String value = header.getValue();
//						if (value != null && value.length() > 40) {
//							value = value.substring(0, 40) + "...";
//						}
//						logger.info(String.format("%s: '%s'", header.getName(), value));
//					}
					logger.info("-----");
				} else {
					logger.info("can not handle multipart mails. (ToT)/~~~");
				}
			}
		} catch (MessagingException e) {
			logger.error("メール受信でエラーが発生。");
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error("メール本文の取得でエラーが発生。");
			logger.error(e.getMessage());
		}
		checkIfTablesExists();
		return RepeatStatus.FINISHED;
	}

	private void checkIfTablesExists() {
		if (mailDao.checkIfTableExists("mail_header")) {
			logger.info("table 'mail_header' exists in maildb.");
		} else {
			logger.error("table 'mail_header' does not exists in maildb.");
		}
		if (mailDao.checkIfTableExists("mail_body")) {
			logger.info("table 'mail_body' exists in maildb.");
		} else {
			logger.error("table 'mail_body' does not exists in maildb.");
		}
	}
}
