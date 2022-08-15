package ducnh.springboot.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import ducnh.springboot.dto.UserDTO;
import ducnh.springboot.model.entity.UserEntity;

@Service
public interface IUserService {
	UserDTO save(UserDTO user);

	void delete(Long[] ids);

	Page<UserDTO> findAllHavingSpec(Specification<UserEntity> spec,Pageable pageable);

}
