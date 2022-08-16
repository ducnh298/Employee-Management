package ducnh.springboot.service;

import ducnh.springboot.dto.UserDTO;
import ducnh.springboot.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface IUserService {
	UserDTO save(UserDTO user);

	void delete(Long[] ids);

	Page<UserDTO> findAll(Pageable pageable);

	UserDTO findById(Long id);

	UserDTO findByCheckinCode(String code,Pageable pageable);

	Page<UserDTO> findAllHavingSpec(Specification<UserEntity> spec,Pageable pageable);

	Page<UserDTO> findByFullName(String key,Pageable pageable);

	Page<UserDTO> findByUserName(String key,Pageable pageable);

	Page<UserDTO> findByRole(String rolename,Pageable pageable);

	Page<UserDTO> findAllByFullnameAndRoleAndAgeDiff(Map<String,String> json, Pageable pageable);

}
