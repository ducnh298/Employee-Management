package ducnh.springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import ducnh.springboot.controller.service.impl.UserDetailServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private UserDetailServiceImpl userDetailServiceImpl;
	
//	@Autowired
//	private AuthenticationEntryPoint entryPoint;

//	@Bean
//	AuthenticationProvider authenticationProvider() {
//		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//		provider.setUserDetailsService(userDetailsService);
//		provider.setPasswordEncoder(new BCryptPasswordEncoder());
//
//		return provider;
//	}
//
//	@Autowired
//	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication().withUser("user1").password(passwordEncoder().encode("12345"))
//				.authorities("USER");
//		auth.inMemoryAuthentication().withUser("admin").password(passwordEncoder().encode("12345"))
//		.authorities("ADMIN");
//	}

	@Bean
	@Override
	public UserDetailsService userDetailsService() {
		// Tạo ra user trong bộ nhớ
		// lưu ý, chỉ sử dụng cách này để minh họa
		// Còn thực tế chúng ta sẽ kiểm tra user trong csdl
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		manager.createUser(User.withDefaultPasswordEncoder() // Sử dụng mã hóa password đơn giản
				.username("admin").password("12345").roles("ADMIN") // phân quyền là người dùng.
				.build());
		return manager;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailServiceImpl) // Cung cáp userservice cho spring security
				.passwordEncoder(passwordEncoder()); // cung cấp password encoder
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests().antMatchers("/", "/login", "/checkin", "/employee-management").permitAll();
		http.authorizeRequests().antMatchers("/employee-management/admin").hasAuthority("USER");
//		http.authorizeRequests().antMatchers("/checkin").hasAnyAuthority("USER","ADMIN");
//		http.authorizeRequests().antMatchers("/employee-management").hasAnyAuthority("ADMIN").anyRequest().authenticated();
//		.and().formLogin()
//				.loginProcessingUrl("/authentication").loginPage("/login").defaultSuccessUrl("/")
//				.failureUrl("/login?error=true").usernameParameter("username").passwordParameter("password").and()
//				.logout().logoutUrl("/logout");
		http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/error");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
