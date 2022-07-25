package ducnh.springboot.controller.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ducnh.springboot.model.dto.UserDTO;

@Service
public interface IUserService {
	UserDTO save(UserDTO user);
	List<UserDTO> findAll();
	void delete(Long[] ids);
	UserDTO findById(Long id);
	UserDTO findByCheckinCode(String checkinCode);
	List<UserDTO> findByFullnameIgnoreCaseContaining(String key);
}
