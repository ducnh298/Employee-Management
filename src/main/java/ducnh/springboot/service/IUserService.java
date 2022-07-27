package ducnh.springboot.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ducnh.springboot.dto.UserDTO;

@Service
public interface IUserService {
	UserDTO save(UserDTO user);
	List<UserDTO> findAll();
	void delete(Long[] ids);
	UserDTO findById(Long id);
	UserDTO findByCheckinCode(String checkinCode);
	UserDTO findByUsername(String username);
	List<UserDTO> findByFullnameIgnoreCaseContaining(String key);
}
