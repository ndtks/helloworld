package com.villageyamada.mailbat.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.villageyamada.mailbat.common.MailbatUtil;

@Component
public class MailDao {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Transactional
	public void regMail(Message<?> msg) {
		if (isMailRegistered(msg)) {
			logger.info("DBに登録済みのメールです。");
			return;
		}
		int id = getNewId(true);
		insertMailHeader(id, msg);
		insertMailBody(id, msg);
	}

	/**
	 * 参照url: http://docs.spring.io/spring-integration/reference/html/mail.html
	 * @param id
	 * @param msg
	 */
	private void insertMailHeader(int id, Message<?> msg) {
		MessageHeaders headers = msg.getHeaders();
		String sql = "insert into mail_header ("
				+ "id, mail_from, mail_to, mail_cc, mail_replyto, mail_subject,"
				+ " mail_raw, mail_contenttype, contenttype, mail_raw_md5,"
				+ " inserted, updated"
				+ ") values ("
				+ ":id, :mail_from, :mail_to, :mail_cc, :mail_replyto, :mail_subject,"
				+ " :mail_raw, :mail_contenttype, :contenttype, :mail_raw_md5,"
				+ " current_timestamp, current_timestamp"
				+ ")";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", id);
		paramMap.put("mail_from", MailbatUtil.convert(headers.get("mail_from")));
		paramMap.put("mail_to", MailbatUtil.convert(headers.get("mail_to")));
		paramMap.put("mail_cc", MailbatUtil.convert(headers.get("mail_cc")));
		paramMap.put("mail_replyto", MailbatUtil.convert(headers.get("mail_replyTo")));
		paramMap.put("mail_subject", MailbatUtil.convert(headers.get("mail_subject")));
		paramMap.put("mail_raw", headers.get("mail_raw").toString());
		paramMap.put("mail_contenttype", MailbatUtil.convert(headers.get("mail_contentType")));
		paramMap.put("contenttype", MailbatUtil.convert(headers.get("contentType")));
		paramMap.put("mail_raw_md5", DigestUtils.md5Hex(headers.get("mail_raw").toString()));
		jdbcTemplate.update(sql, paramMap);
	}

	private void insertMailBody(int id, Message<?> msg) {
		String mailContentType = (String)msg.getHeaders().get("mail_contentType");
		String contentType = (String)msg.getHeaders().get("contentType");
		if (mailContentType == null || !mailContentType.equals(contentType)) {
			logger.info("テキストメールでない可能性があり、メール本文をDBに登録しません。");
			logger.info(String.format("mail_contentType: %s, contentType: %s", mailContentType, contentType));
			return;
		}
		String sql = "insert into mail_body ("
				+ "id, body_text, inserted, updated"
				+ ") values ("
				+ ":id, :body_text, current_timestamp, current_timestamp"
				+ ")";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", id);
		paramMap.put("body_text", (String)msg.getPayload());
		jdbcTemplate.update(sql, paramMap);
	}

	/**
	 * DBに登録されているIDの最大値を取得する。
	 * @param fromMailHeader true: MailHeaderテーブルから最初に取得する, false: MailBodyテーブルから最初に取得する
	 * @return
	 */
	private int getNewId(boolean fromMailHeader) {
		String sql = "select case when max(id) is null then 1 else max(id) + 1 end as new_id";
		if (fromMailHeader) {
			sql += " from mail_header";
		} else {
			sql += " from mail_body";
		}
		Map<String, Object> paramMap = new HashMap<>();
		int newId = jdbcTemplate.queryForObject(sql, paramMap, Integer.class);
		paramMap.put("id", newId);
		sql = "select count(*)";
		if (fromMailHeader) {
			sql += " from mail_body where id = :id";
		} else {
			sql += " from mail_header where id = :id";
		}
		int count = jdbcTemplate.queryForObject(sql, paramMap, Integer.class);
		if (count == 0) {
			return newId;
		} else {
			return getNewId(!fromMailHeader);
		}
	}

	/**
	 * メールが既にDBに登録されているかチェックする。
	 * @param msg
	 * @return true:登録済 / false:未登録
	 */
	public boolean isMailRegistered(Message msg) {
		String sql = "select count(*) from mail_header"
				+ " where mail_raw_md5 = :mailRawMd5";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("mailRawMd5", DigestUtils.md5Hex(msg.getHeaders().get("mail_raw").toString()));
		int count = jdbcTemplate.queryForObject(sql, paramMap, Integer.class);
		return count > 0;
	}
}
