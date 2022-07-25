package ducnh.springboot.controller.service;

import org.springframework.stereotype.Service;

import ducnh.springboot.model.dto.RoleDTO;

@Service
public interface IRoleService {
	RoleDTO findById(Long id);
	RoleDTO save(RoleDTO role);
}
