package ducnh.springboot.controller.service.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ducnh.springboot.controller.repository.UserRepository;
import ducnh.springboot.model.entity.UserEntity;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity user = userRepository.findUserByUsername(username);
		if (user == null) {
			System.out.println("User not found!" + username);
			throw new UsernameNotFoundException("User " + username + " was not found in the database");
		}
		System.out.println("Found user!" + username);
        UserDetails userDetails = new User(user.getUsername(), user.getPassword(), (Collection<? extends GrantedAuthority>) user.getRoles());
        System.out.println(userDetails);
        return userDetails;
	}

//	public Set<SimpleGrantedAuthority> getRole(UserEntity user) {
//        String role = user.getMainRole();
//        return Collections.singleton(new SimpleGrantedAuthority(role));
//    }
}
