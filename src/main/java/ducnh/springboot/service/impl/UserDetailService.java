package ducnh.springboot.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ducnh.springboot.model.entity.RoleEntity;
import ducnh.springboot.model.entity.UserEntity;
import ducnh.springboot.repository.UserRepository;

@Service
public class UserDetailService implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity user = userRepository.findByUsername(username);
		if (user == null) {
			System.out.println("User not found!" + username);
			throw new UsernameNotFoundException("User " + username + " was not found in the database");
		}
		System.out.println("Found user! " + username);
		
		Set<GrantedAuthority> grantedAuthorities = new HashSet();
        Set<RoleEntity> roles = user.getRoles();
		
        for(RoleEntity role: roles) 
        	grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        
		UserDetails  userDetails = new User(user.getUsername(), user.getPassword(), grantedAuthorities);
		System.out.println(userDetails);
		return userDetails;
	}

//	public UserDetails loadUserById(Long id) throws Exception {
//		UserEntity user = userRepository.findById(id).orElse(null);
//		if (user == null) {
//			System.out.println("User not found!" + id);
//			throw new Exception("User " + id + " was not found in the database");
//		}
//		System.out.println("Found user!" + id);
//		UserDetails userDetails = new User(user.getUsername(), user.getPassword(),
//				new ArrayList<GrantedAuthority>());
//		System.out.println(userDetails);
//		return userDetails;
//	}

}
