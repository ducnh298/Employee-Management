package ducnh.springboot.controller.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ducnh.springboot.controller.converter.UserConverter;
import ducnh.springboot.controller.repository.RoleRepository;
import ducnh.springboot.controller.repository.UserRepository;
import ducnh.springboot.controller.service.IUserService;
import ducnh.springboot.model.dto.RoleDTO;
import ducnh.springboot.model.dto.UserDTO;
import ducnh.springboot.model.entity.RoleEntity;
import ducnh.springboot.model.entity.UserEntity;
import ducnh.springboot.utils.RandomUtils;

@Service
public class UserService implements IUserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	UserConverter converter;

	@Autowired
	RandomUtils randomUtils;

	public UserDTO save(UserDTO user) {
		UserEntity userEntity = new UserEntity();

		if (user.getRoles() != null) {
			user.setRoles(user.getRoles().stream().map(role -> {
				RoleEntity roleEntity = roleRepository.findById(role.getId()).orElse(null);
				return modelMapper.map(roleEntity, RoleDTO.class);
			}).collect(Collectors.toSet()));
		}

		if (user.getId() != null) {
			UserEntity oldUserEntity = userRepository.findById(user.getId()).orElse(null);
			userEntity = converter.toEntity(user, oldUserEntity);
		} else {
			String code = "";
			while (userRepository.findByCheckinCode(code) != null || code.equals("")) {
				code = randomUtils.randCheckinCode();
			}
			user.setCheckinCode(code.toString());
			userEntity = modelMapper.map(user, UserEntity.class);
		}
		
		userEntity = userRepository.save(userEntity);
		user = modelMapper.map(userEntity, UserDTO.class);
		return user;
	}

	public List<UserDTO> findAll() {
		List<UserEntity> list = (List<UserEntity>) userRepository.findAll();
		List<UserDTO> listDTO = new ArrayList<UserDTO>();
		for (UserEntity item : list)
			listDTO.add(modelMapper.map(item, UserDTO.class));
		return listDTO;
	}

	public void delete(Long[] ids) {
		for (Long id : ids)
			userRepository.deleteById(id);
	}

	public UserDTO findById(Long id) {
		UserEntity entity = userRepository.findById(id).orElse(null);
		UserDTO user = modelMapper.map(entity, UserDTO.class);

		return user;
	}

	public UserDTO findByCheckinCode(String checkinCode) {
		try {
			return modelMapper.map(userRepository.findByCheckinCode(checkinCode), UserDTO.class);
		} catch (NullPointerException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public List<UserDTO> findByFullnameIgnoreCaseContaining(String key) {
		List<UserEntity> entities = userRepository.findByFullnameIgnoreCaseContaining(key);
		List<UserDTO> result = new ArrayList<UserDTO>();
		for (UserEntity entity : entities) {
			result.add(modelMapper.map(entity, UserDTO.class));
		}
		return result;
	}

}
