package ducnh.springboot.config;

import ducnh.springboot.dto.CustomOAuth2User;
import ducnh.springboot.service.IUserService;
import ducnh.springboot.service.impl.CustomOAuth2UserService;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import ducnh.springboot.security.jwt.JWTFilter;
import ducnh.springboot.service.impl.UserDetailService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailService userDetailService;

    @Autowired
    IUserService userService;

    @Autowired
    private JWTFilter jwtFilter;

    @Autowired
    CustomOAuth2UserService oAuth2UserService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests().antMatchers("/home", "/authenticate").permitAll()
                .antMatchers("/oauth/**").permitAll()
                .antMatchers("/employee-management/find/*").permitAll()
                .antMatchers(HttpMethod.GET, "/employee-management/").hasAnyRole("HR", "STAFF")
                .antMatchers(HttpMethod.GET,"/working-hour/*").hasAnyRole("HR","STAFF","INTERN")
                //.antMatchers("/request-off/*").hasAnyRole("HR","STAFF","INTERN")

                // .antMatchers(HttpMethod.POST,"/checkin/**").hasAnyRole("HR","STAFF","INTERN")
                // .antMatchers("/checkin/*").hasAnyRole("HR","STAFF","INTERN")
                .anyRequest().authenticated().and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .oauth2Login().loginPage("/login")
                .userInfoEndpoint()
                .userService(oAuth2UserService).and()
                .successHandler(new AuthenticationSuccessHandler() {

                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
                        userService.processOAuthPostLogin(oauthUser.getName(),oauthUser.getEmail());
                        response.sendRedirect("/home");
                    }


                });

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

//		http.authorizeRequests().and().formLogin().loginPage("/login").loginProcessingUrl("/authentication")
//				.defaultSuccessUrl("/").failureUrl("/login?error=true").usernameParameter("username")
//				.passwordParameter("password").and().logout().logoutUrl("/logout");
//		http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/error");
    }

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
