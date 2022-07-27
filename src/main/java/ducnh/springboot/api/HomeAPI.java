package ducnh.springboot.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ducnh.springboot.dto.JWTResponse;
import ducnh.springboot.dto.UserDTO;
import ducnh.springboot.security.jwt.JWTProvider;
import ducnh.springboot.service.impl.UserDetailService;

@RestController
public class HomeAPI {

	@Autowired
	JWTProvider jwtProvider;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserDetailService userDetailService;

	@GetMapping("/home")
	public String homePage() {
		return "Welcome!";
	}

	@PostMapping("/authenticate")
	public JWTResponse authenticate(@RequestBody UserDTO user) throws Exception {

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
		UserDetails userDetails = userDetailService.loadUserByUsername(user.getUsername());
		String token = jwtProvider.generateToken(userDetails);
		
		return new JWTResponse(token);
	}
}
