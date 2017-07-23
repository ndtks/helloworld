package com.villageyamada.mailbat.common;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MailbatUtil {

	private static final Logger logger = LoggerFactory.getLogger(MailbatUtil.class);

	private static String[] charsets = new String[] {
			"ISO-2022-JP",
			"UTF-8",
			"EUC-JP",
			"Shift_JIS"
	};

	public static Object convert(Object obj) {
		if (!(obj instanceof String) && !(obj instanceof String[])) {
			return (String)obj;
		}
		if (obj instanceof String) {
			return decode((String)obj);
		} else {
			StringBuilder sb = new StringBuilder();
			for (String str : (String[])obj) {
				if (sb.length() > 0) {
					sb.append(",");
				}
				sb.append(decode(str));
			}
			return sb.toString();
		}
	}

	private static String decode(String str) {
		String charset = null;
		for (String c : charsets) {
			if (str != null && str.toLowerCase().indexOf("=?" + c.toLowerCase() + "?b?") == 0
					&& str.indexOf("?=") > 0) {
				try {
					return new String(Base64.getDecoder().decode(
							str.substring(("=?" + c + "?b?").length(), str.indexOf("?="))), c)
							+ str.substring(str.indexOf("?=") + 2);
				} catch (UnsupportedEncodingException e) {
					logger.error("ISO-2022-JPでのdecodeに失敗。", e);
					return str;
				}
			}
		}
		return str;
	}
}
