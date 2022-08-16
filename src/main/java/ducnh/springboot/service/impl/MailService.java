package ducnh.springboot.service.impl;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import ducnh.springboot.service.IMailService;

@Service
public class MailService implements IMailService {

	@Autowired
	JavaMailSender mailSender;

	@Override
	public String sendMail(String email, String subject, String content) {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(message, true, "utf-8");
			String htmlMsg = content + "<br><h2><b>Have a nice day!</b><br><font color=purple><b>Komu<b></font></h2>"
					+ "<br><img src='https://ncc.asia/images/logo/logo.png'>";
			message.setContent(htmlMsg, "text/html");

			FileSystemResource file = new FileSystemResource(new File("test.txt"));
			helper.addAttachment("Demo Mail", file);

			helper.setTo("barusu2982@gmail.com");
			helper.setSubject(subject);

			mailSender.send(message);
			return "Email Sent Sucessfully!";
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Email sent unsuccessfully!";

	}

}
