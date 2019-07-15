package br.com.murilo.services.email;

import java.util.Date;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import br.com.murilo.domain.User;
import br.com.murilo.domain.VerificationToken;
import br.com.murilo.service.exception.ObjectNotFoundException;
import br.com.murilo.services.UserService;

public abstract class AbstractEmailService implements EmailService {

	@Autowired
	private TemplateEngine template;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private UserService userService;

	@Value("default.sender")
	private String sender;

	@Value("default.url")
	private String contextPath;

	@Override
	public void sendConfirmationHtmlEmail(User user, VerificationToken token) {
		try {
			sendHtmlEmail(prepareMimeMessageFromUser(user, token));
		} catch (MessagingException msg) {
			throw new ObjectNotFoundException("Erro ao tentar enviar o email");
		}
	}

	protected MimeMessage prepareMimeMessageFromUser(User user, VerificationToken token) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setTo(user.getEmail());
		helper.setFrom(this.sender);
		helper.setSubject("Confirmação de registro");
		helper.setSentDate(new Date(System.currentTimeMillis()));
		helper.setText(htmlFromTemplateUser(user, token), true);
		return message;
	}

	protected String htmlFromTemplateUser(User user, VerificationToken token) {
		String uuid = UUID.randomUUID().toString();

		if (token == null) {
			userService.createVerificationTokenForUser(user, uuid);
		} else {
			uuid = token.getToken();
		}

		String confirmationURL = this.contextPath + "/api/public/registration/users?token=" + uuid;
		Context context = new Context();
		context.setVariable("user", user);
		context.setVariable("confirmationUrl", confirmationURL);
		return template.process("email/registerUser", context);
	}
}
