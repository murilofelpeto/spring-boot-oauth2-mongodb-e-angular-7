package br.com.murilo.services.email;

import javax.mail.internet.MimeMessage;

import br.com.murilo.domain.User;
import br.com.murilo.domain.VerificationToken;

public interface EmailService {
	
	void sendHtmlEmail(MimeMessage message);
	void sendConfirmationHtmlEmail(User user, VerificationToken token);

}
