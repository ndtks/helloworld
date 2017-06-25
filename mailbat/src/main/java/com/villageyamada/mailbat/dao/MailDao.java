package com.villageyamada.mailbat.dao;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.Message;
import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class MailDao {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public void regMail(Message msg) {
		int newId = getNewId(true);
		insertMailHeader(newId, msg);
		insertMailBody(newId, msg);
	}

	/**
	 * IDの最大値を取得する。
	 * @param fromMailHeader true: MailHeaderテーブルから取得する, false: MailBodyテーブルから取得する
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

	private void insertMailHeader(int id, Message msg) {
		String sql = "insert into mail_header ("
				+ "id, mail_from, mail_subject, mail_contenttype, inserted, updated"
				+ ") values ("
				+ ":id, :mail_from, :mail_subject, :mail_contenttype, current_timestamp, current_timestamp"
				+ ")";
		Map<String, Object> paramMap = new HashMap<>();
		try {
			paramMap.put("id", id);
			paramMap.put("mail_from", msg.getFrom()[0].toString());
			paramMap.put("mail_subject", msg.getSubject());
			paramMap.put("mail_contenttype", msg.getContentType());
		} catch (MessagingException e) {
		}
		jdbcTemplate.update(sql, paramMap);
	}

	private void insertMailBody(int id, Message msg) {
		String sql = "insert into mail_body ("
				+ "id, body_text, inserted, updated"
				+ ") values ("
				+ ":id, :body_text, current_timestamp, current_timestamp"
				+ ")";
		Map<String, Object> paramMap = new HashMap<>();
		try {
			paramMap.put("id", id);
			paramMap.put("body_text", msg.getContent().toString());
		} catch (MessagingException e) {
		} catch (IOException e) {
		}
		jdbcTemplate.update(sql, paramMap);
	}

	public boolean checkIfTableExists(String tableName) {
		String sql = "select count(*) from information_schema.tables"
				+ " where table_schema = 'maildb' and table_name = :tableName";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("tableName", tableName);
		int count = jdbcTemplate.queryForObject(sql, paramMap, Integer.class);
		return count > 0;
	}

	public boolean checkIfMailRegistered(Message msg) {
		String sql = "select count(*) from mail_header"
				+ " where mail_from = :mailFrom and ";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("", "");
		int count = jdbcTemplate.queryForObject(sql, paramMap, Integer.class);
		return count > 0;
	}
}
