package ducnh.springboot.service;

import org.springframework.stereotype.Service;


public interface IMailService {
	String sendMail(String email, String subject, String content);
}
