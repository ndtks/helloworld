package com.villageyamada.mailbat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableAutoConfiguration(exclude={HibernateJpaAutoConfiguration.class})
public class MailbatApplication {

	public static void main(String[] args) {
		SpringApplication.run(MailbatApplication.class, args);
	}
}
