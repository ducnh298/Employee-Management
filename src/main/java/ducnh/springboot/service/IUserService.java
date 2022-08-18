package ducnh.springboot.service;

import ducnh.springboot.dto.UserDTO;
import ducnh.springboot.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface IUserService {
	UserDTO save(UserDTO user);

	void delete(Long[] ids);

	List<UserDTO> findAll();
	Page<UserDTO> findAll(Pageable pageable);

	Page<UserDTO> findAll(Specification spec,Pageable pageable);

	UserDTO findById(Long id);

	<T> T findByCheckinCode(Class<T> clastype,String code);


}
