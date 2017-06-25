package com.villageyamada.mailbat.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.mail.Message;
import javax.mail.MessagingException;

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

	/**
	 * ID
	 */
	private int id;

	/**
	 * FROMアドレス
	 */
	private String mailFrom;

	/**
	 * TOアドレス
	 */
	private String mailTo;

	/**
	 * CCアドレス
	 */
	private String mailCc;

	/**
	 * REPLY-TOアドレス
	 */
	private String replyto;

	/**
	 * 件名
	 */
	private String mailSubject;

	/**
	 * 行数
	 */
	private int mailLineCount;

	/**
	 * 受信日時
	 */
	private Date mailReceivedDate;

	/**
	 * サイズ
	 */
	private int mailSize;

	/**
	 * メールヘッダーの生データ
	 */
	private String mailRaw;

	/**
	 * メールのContentType
	 */
	private String mailContenttype;

	/**
	 * PayLoadのContentType
	 */
	private String contenttype;

	/**
	 * mailRawのMD5値
	 */
	private String mailRawMd5;

	/**
	 * DB登録日時
	 */
	private Timestamp inserted;

	/**
	 * DB更新日時
	 */
	private Timestamp updated;

	public MailHeader(Message msg) {
		if (msg == null) {
			return;
		}
		try {
			if (msg.getFrom() != null && msg.getFrom().length > 0) {
				this.mailFrom = msg.getFrom()[0].toString();
			}
			this.mailSubject = msg.getSubject();
			this.mailLineCount = msg.getLineCount();
			this.mailReceivedDate = msg.getReceivedDate();
			this.mailSize = msg.getSize();
		} catch (MessagingException e) {
		}
	}
}
