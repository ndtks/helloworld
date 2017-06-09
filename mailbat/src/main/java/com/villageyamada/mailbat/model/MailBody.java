package com.villageyamada.mailbat.model;

import java.io.Serializable;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MailBody implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String bodyText;
	private Timestamp inserted;
	private Timestamp updated;
}
