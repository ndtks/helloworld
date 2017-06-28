package com.villageyamada.mailbat.tasklet;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.mail.Pop3MailReceiver;
import org.springframework.integration.mail.support.DefaultMailHeaderMapper;
import org.springframework.messaging.Message;
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
			receiver.setHeaderMapper(new DefaultMailHeaderMapper());
			Message<?>[] messages = (Message<?>[]) receiver.receive();
			for (Message<?> msg : messages) {
				if (msg == null) {
					continue;
				}
				if (((String)msg.getHeaders().get("contentType")).equals("application/octet-stream")) {
					logger.info("添付ファイルありのメールはひとまず無視します。");
					continue;
				}
				mailDao.regMail(msg);
			}
		} catch (MessagingException e) {
			logger.error("メール受信でエラーが発生。");
			logger.error(e.getMessage());
		}
		return RepeatStatus.FINISHED;
	}
}
