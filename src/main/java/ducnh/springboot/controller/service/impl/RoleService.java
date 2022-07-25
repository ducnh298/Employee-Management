package ducnh.springboot.controller.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ducnh.springboot.controller.repository.RoleRepository;
import ducnh.springboot.controller.service.IRoleService;
import ducnh.springboot.model.dto.RoleDTO;
import ducnh.springboot.model.entity.RoleEntity;

@Service
public class RoleService implements IRoleService{

	@Autowired
	ModelMapper mapper;
	
	@Autowired
	RoleRepository roleRepository;
	
	public RoleDTO findById(Long id) {
		return mapper.map(roleRepository.findById(id), RoleDTO.class);
	}

	@Override
	public RoleDTO save(RoleDTO role) {
		RoleEntity entity = mapper.map(role, RoleEntity.class);
		return mapper.map(roleRepository.save(entity), RoleDTO.class);
	}

}
