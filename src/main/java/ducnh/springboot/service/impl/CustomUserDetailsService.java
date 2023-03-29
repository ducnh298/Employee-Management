package ducnh.springboot.service.impl;

import ducnh.springboot.model.entity.RoleEntity;
import ducnh.springboot.model.entity.UserEntity;
import ducnh.springboot.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    Logger log = LoggerFactory.getLogger(getClass());

    @Override
    @Cacheable(key = "#username", value = "jwt")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(UserEntity.class, username);
        if (user == null) {
            System.out.println("User not found!" + username);
            throw new UsernameNotFoundException("User " + username + " was not found in the database");
        }
        log.info("Found user! " + username);

        Set<GrantedAuthority> grantedAuthorities = new HashSet();
        Set<RoleEntity> roles = user.getRoles();

        for (RoleEntity role : roles)
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));

        UserDetails userDetails = new User(user.getUsername(), user.getPassword(), grantedAuthorities);
        log.info(String.valueOf(userDetails));
        return userDetails;
    }

//	public UserDetails loadUserById(Long id) throws Exception {
//		UserEntity user = userRepository.findById(id).orElse(null);
//		if (user == null) {
//			log.info("User not found!" + id);
//			throw new Exception("User " + id + " was not found in the database");
//		}
//		log.info("Found user!" + id);
//		UserDetails userDetails = new User(user.getUsername(), user.getPassword(),
//				new ArrayList<GrantedAuthority>());
//		log.info(userDetails);
//		return userDetails;
//	}

}
