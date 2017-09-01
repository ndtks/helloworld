package com.villageyamada.mailbat.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MailReceiveService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * DBに登録された全てのアカウントのメールを受信する。
	 */
	public void receiveAll() {
	}

	/**
	 * アカウントIDで指定されたアカウントのメールを受信する。
	 *
	 * @param accId アカウントID
	 */
	public void receiveById(String accId) {
	}

	/**
	 * メールアドレスで指定されたアカウントのメールを受信する。
	 *
	 * @param addr メールアドレス
	 */
	public void receiveByAddr(String addr) {
	}
}
