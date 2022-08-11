package ducnh.springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import ducnh.springboot.security.jwt.JWTFilter;
import ducnh.springboot.service.impl.UserDetailService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailService userService;

	@Autowired
	private JWTFilter jwtFilter;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests().antMatchers("/home", "/authenticate").permitAll()
//		.antMatchers("/employee-management","/sendmail/*").hasRole("HR")
		.antMatchers(HttpMethod.GET,"/employee-management").hasAnyRole("HR","STAFF")
				// .antMatchers(HttpMethod.POST,"/checkin/**").hasAnyRole("HR","STAFF","INTERN")
				// .antMatchers("/checkin/*").hasAnyRole("HR","STAFF","INTERN")
				.anyRequest().authenticated().and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

//		http.authorizeRequests().and().formLogin().loginPage("/login").loginProcessingUrl("/authentication")
//				.defaultSuccessUrl("/").failureUrl("/login?error=true").usernameParameter("username")
//				.passwordParameter("password").and().logout().logoutUrl("/logout");
//		http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/error");
	}

//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.csrf().disable();
//		http.authorizeRequests().antMatchers("/", "/login", "/login", "/checkin", "/employee-management").permitAll();
//		http.authorizeRequests().antMatchers("/employee-management/admin").hasRole("ADMIN").and()
//				.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
////		http.authorizeRequests().antMatchers("/checkin").hasAnyAuthority("INTERN","ADMIN");
////		http.authorizeRequests().antMatchers("/employee-management").hasAnyAuthority("ADMIN").anyRequest().authenticated();
//		http.authorizeRequests().and().formLogin().loginPage("/login").loginProcessingUrl("/authentication").defaultSuccessUrl("/")
//				.failureUrl("/login?error=true").usernameParameter("username").passwordParameter("password").and()
//				.logout().logoutUrl("/logout");
//		http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/error");
//	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {

		return super.authenticationManagerBean();
	}

}
