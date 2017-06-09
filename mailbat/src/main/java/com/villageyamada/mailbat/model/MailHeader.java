package com.villageyamada.mailbat.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * http://docs.spring.io/spring-integration/reference/html/mail.html
 *
 * @author donbe
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MailHeader implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String mailFrom;
	private String mailBcc;
	private String mailCc;
	private String replyto;
	private String mailSubject;
	private int mailLineCount;
	private Date mailReceivedDate;
	private int mailSize;
	private String mailExpunged;
	private String mailRaw;
	private String mailContenttype;
	private String contenttype;
	private String mailRawMd5;
	private Timestamp inserted;
	private Timestamp updated;
}
