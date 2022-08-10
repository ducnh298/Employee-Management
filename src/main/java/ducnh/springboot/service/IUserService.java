package ducnh.springboot.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ducnh.springboot.dto.UserDTO;

@Service
public interface IUserService {
	UserDTO save(UserDTO user);
	<T> List<T> findAll(Class<T> classtype);
	void delete(Long[] ids);
	<T> T findById(Class<T> classtype,Long id);
	<T> T findByCheckinCode(Class<T> classtype,String checkinCode);
	<T> T findByUsername(Class<T> classtype,String username);
	<T> List<T> findByFullnameIgnoreCaseContaining(Class<T> classtype,String key);
	<T> List<T> findAllOrderByFullnameASC(Class<T> classtype);
	<T> List<T> findAllOrderByFullnameDESC(Class<T> classtype);
	List<UserDTO> findAllEmployeeHavingRole(String roleName);
}
