package ducnh.springboot.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import ducnh.springboot.dto.RoleDTO;
import ducnh.springboot.projection.IRoleCount;

@Service
public interface IRoleService {
	RoleDTO findById(Long id);

	RoleDTO save(RoleDTO role);

	List<IRoleCount> RoleCountEm();

	Slice<RoleDTO> findByNameContaining(String name, Pageable pageable);

	<T> Page<T> findByNameAndDetail(Map<String,String> json, Pageable pageable, Class<T> classtype);
}
