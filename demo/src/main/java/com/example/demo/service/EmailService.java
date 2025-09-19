package com.example.demo.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.example.demo.entity.Mail;
import com.example.demo.entity.User;
import com.example.demo.repositories.EmailRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.service.exception.EmailException;
import com.example.demo.service.exception.ExceptionOfExistingEmail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService implements EmailRepository {

	private final JavaMailSender mailSender;
	private final TemplateEngine templateEngine;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TicketService ticketService;

	public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
		this.mailSender = mailSender;
		this.templateEngine = templateEngine;
	}

	@Override
	public void sendHTMLEmail(Mail mail) {
		
		boolean exist = this.userRepository.existsByLogin(mail.getTo());
		
		if (!exist) {
			
			throw new ExceptionOfExistingEmail("email already exists");
		}
		
		try {

			String ticket = ticketService.buildAndSaveTicket(mail.getTo());

			Context context = new Context();
			context.setVariable("username", mail.getTo());
			context.setVariable("ticket", ticket);

			String process = templateEngine.process("index", context);

			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);

			helper.setSubject("Redefinir senha");
			helper.setFrom("gustavo.teles711@gmail.com");
			helper.setText(process, true);
			helper.setTo(mail.getTo());

			mailSender.send(message);

		} catch (RuntimeException error) {

			System.out.println("Erro " + error);
			throw new EmailException("Unknown error");
		} catch (AddressException e) {

			e.printStackTrace();
			throw new EmailException("error with recipient");
		} catch (MessagingException e) {

			e.printStackTrace();
			throw new EmailException("Error sending email");
		}

	}

}
