package ducnh.springboot.service;

import org.springframework.stereotype.Service;

import ducnh.springboot.dto.RoleDTO;

@Service
public interface IRoleService {
	RoleDTO findById(Long id);
	RoleDTO save(RoleDTO role);
}
