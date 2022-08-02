package ducnh.springboot.service;

import org.springframework.stereotype.Service;

@Service
public interface IMailService {
	public String sendMail(String email, String subject, String content);
}
