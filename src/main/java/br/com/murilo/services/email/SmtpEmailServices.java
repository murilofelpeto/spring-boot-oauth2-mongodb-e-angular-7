package br.com.murilo.services.email;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

public class SmtpEmailServices extends AbstractEmailService {

	@Autowired
	private JavaMailSender javaMailSender;
	
	private static final Logger LOG = LoggerFactory.getLogger(SmtpEmailServices.class);
	
	@Override
	public void sendHtmlEmail(MimeMessage message) {
		LOG.info("Enviando email");
		javaMailSender.send(message);
		LOG.info("Email enviado com sucesso");
	}

}
