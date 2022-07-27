package ducnh.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EntityScan("ducnh.springboot.model.entity")
@SpringBootApplication
@EnableWebSecurity
public class Application {

	public static void main(String[] args) {

		SpringApplication.run(Application.class, args);

	}


}