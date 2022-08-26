package ducnh.springboot.controller;

import ducnh.springboot.dto.JWTResponse;
import ducnh.springboot.security.jwt.JWTProvider;
import ducnh.springboot.service.IMailService;
import ducnh.springboot.service.impl.CustomeUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.util.Map;

@RestController
public class HomeRestController {

    @Autowired
    JWTProvider jwtProvider;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CustomeUserDetailsService userDetailService;

    @Autowired
    IMailService mailService;

    @GetMapping("/home")
    public String homePage() {

        return "Welcome!";
    }

    @PostMapping("/authenticate")
    public JWTResponse authenticate(@RequestBody Map<String, String> user) throws Exception {
        System.out.println("Authenticating...");
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(user.get("username"), user.get("password")));

        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
        UserDetails userDetails = userDetailService.loadUserByUsername(user.get("username"));
        String token = jwtProvider.generateToken(userDetails);

        return new JWTResponse(token);
    }

    @PostMapping("/sendmail")
    public ResponseEntity<String> sendEmail(@RequestBody Map<String, String> json) throws MessagingException {
        String subject = json.get("subject");
        String content = json.get("content");

        return new ResponseEntity<>(mailService.sendMail("barusu2982@gmail.com", subject, content), HttpStatus.OK);
    }
}
