package ducnh.springboot.service;

import ducnh.springboot.dto.UserDTO;
import ducnh.springboot.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;


public interface IUserService {
	UserDTO save(UserEntity user);

	void delete(Long[] ids);

	List<UserDTO> findAll();
	Page<UserDTO> findAll(Pageable pageable);

	Page<UserDTO> findAll(Specification spec,Pageable pageable);

	UserDTO findById(Long id);

	<T> T findByCheckinCode(Class<T> clastype,String code);

	UserDTO deleteRoles(Long userId, Long[] roleIds);

	List<UserDTO> findAllForgetCheckin(Timestamp today,Timestamp tomorrow);

	List<UserDTO> findAllForgetCheckout(Timestamp today,Timestamp tomorrow);

	void processOAuthPostLogin(String name,String email);
}
