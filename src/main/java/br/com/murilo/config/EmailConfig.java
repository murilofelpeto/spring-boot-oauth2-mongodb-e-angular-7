package br.com.murilo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import br.com.murilo.services.email.EmailService;
import br.com.murilo.services.email.SmtpEmailServices;

@Configuration
@PropertySource("classpath:application.properties")
public class EmailConfig {

	@Bean
	public EmailService emailService() {
		return new SmtpEmailServices();
	}
}
