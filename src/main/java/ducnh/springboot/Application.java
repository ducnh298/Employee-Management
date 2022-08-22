package ducnh.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

@EntityScan("ducnh.springboot.model.entity")
@SpringBootApplication
@EnableWebSecurity
@EnableScheduling
@EnableCaching
@EnableAsync
public class Application {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(Application.class);
		//application.setWebApplicationType(WebApplicationType.REACTIVE);
		application.run(args);
	}

}