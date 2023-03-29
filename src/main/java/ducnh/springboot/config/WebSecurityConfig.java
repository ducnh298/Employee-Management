package ducnh.springboot.config;

import ducnh.springboot.security.jwt.JWTFilter;
import ducnh.springboot.service.IUserService;
import ducnh.springboot.service.impl.CustomOAuth2UserService;
import ducnh.springboot.service.impl.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    CustomUserDetailsService userDetailService;

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
                .antMatchers("/employee-management/find/").permitAll()
                .antMatchers(HttpMethod.GET, "/employee-management/").hasAnyRole("HR", "STAFF")
                .antMatchers(HttpMethod.GET, "/working-hour/").hasAnyRole("HR", "STAFF", "INTERN")
                .antMatchers(HttpMethod.POST, "/request-off/").hasAnyRole("HR", "STAFF", "INTERN")
                .antMatchers(HttpMethod.POST, "/request-working-hour/").hasAnyRole("HR", "STAFF", "INTERN")
                .antMatchers("/checkin/").hasAnyRole("HR", "STAFF", "INTERN", "PM")
                .anyRequest().authenticated().and().logout().logoutSuccessUrl("/home");
//                .and().sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//                .oauth2Login().loginPage("/login")
//                .userInfoEndpoint()
//                .userService(oAuth2UserService).and()
//                .successHandler(new AuthenticationSuccessHandler() {
//
//                    @Override
//                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//                        CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
//                        userService.processOAuthPostLogin(oauthUser.getName(),oauthUser.getEmail());
//                        response.sendRedirect("/home");
//                    }
//
//
//                });

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
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
