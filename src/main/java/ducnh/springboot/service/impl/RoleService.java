package ducnh.springboot.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ducnh.springboot.converter.RoleConverter;
import ducnh.springboot.dto.RoleDTO;
import ducnh.springboot.model.entity.RoleEntity;
import ducnh.springboot.projection.IRoleCount;
import ducnh.springboot.repository.RoleRepository;
import ducnh.springboot.service.IRoleService;

@Service
public class RoleService implements IRoleService {

	@Autowired
	ModelMapper mapper;

	@Autowired
	RoleConverter converter;

	@Autowired
	RoleRepository roleRepository;

	@Override
	public RoleDTO save(RoleDTO role) {
		RoleEntity entity = mapper.map(role, RoleEntity.class);
		return converter.toDTO(roleRepository.save(entity));
	}

	@Override
	public RoleDTO findById(Long id) {
		System.out.println("role found by id: " + id + " \n" + roleRepository.findById(id).toString());
		return converter.toDTO(roleRepository.findById(id).orElse(null));
	}

	@Override
	public List<IRoleCount> RoleCountEm() {
//		return null;

		return roleRepository.RoleCountEm();
	}

}
