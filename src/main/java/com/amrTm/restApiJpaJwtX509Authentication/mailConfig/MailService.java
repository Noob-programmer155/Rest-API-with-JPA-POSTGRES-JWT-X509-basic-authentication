package com.amrTm.restApiJpaJwtX509Authentication.mailConfig;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {
	private JavaMailSender javaMailSender;
	public MailService(JavaMailSender javaMailSender) {
		super();
		this.javaMailSender = javaMailSender;
	}
	
	public void sendMessage(String to, String from, String subject, String text) throws MessagingException {
		MimeMessage msg = javaMailSender.createMimeMessage();
		MimeMessageHelper hg = new MimeMessageHelper(msg);
		hg.setTo(to);
		hg.setFrom(from);
		hg.setSubject(subject);
		hg.setText(text, false);
		javaMailSender.send(msg);
	}
	
//	public void sendValidationMessage(String to, String name, String token, String id) throws MessagingException {
//		MimeMessage mime = javaMailSender.createMimeMessage();
//		MimeMessageHelper msg = new MimeMessageHelper(mime);
//		msg.setTo(to);
//		msg.setSubject("Email Validation");
//		msg.setText("<h4>Hello <strong>"+name+"</strong>,</h4><br/><hr/>"
//				+ "<h6>Please confirm your email with this link:</h6><br/>"
//				+ "<a href='http://localhost:6753/signup/confirm?token="+token+"&id="+id+"' type='button'>click me</a>", true);
//		javaMailSender.send(mime);
//	}
}
