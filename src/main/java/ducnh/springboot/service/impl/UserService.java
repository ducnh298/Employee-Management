package ducnh.springboot.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ducnh.springboot.converter.UserConverter;
import ducnh.springboot.dto.RoleDTO;
import ducnh.springboot.dto.UserDTO;
import ducnh.springboot.model.entity.RoleEntity;
import ducnh.springboot.model.entity.UserEntity;
import ducnh.springboot.repository.RoleRepository;
import ducnh.springboot.repository.UserRepository;
import ducnh.springboot.service.IRoleService;
import ducnh.springboot.service.IUserService;
import ducnh.springboot.utils.RandomUtils;

@Service
public class UserService implements IUserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	IRoleService roleService;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	UserConverter converter;

	@Autowired
	RandomUtils randomUtils;

	@Transactional(rollbackOn = Exception.class)
	public UserDTO save(UserDTO user) {
		UserEntity userEntity = new UserEntity();

		if (user.getRoles() != null) {
			user.setRoles(user.getRoles().stream().map(role -> {
				return roleService.findById(role.getId());
			}).collect(Collectors.toSet()));
		}

		if (user.getId() != null) {
			UserEntity oldUserEntity = userRepository.findById(user.getId()).orElse(null);
			userEntity = converter.toEntity(user, oldUserEntity);
		} else {
			userEntity = modelMapper.map(user, UserEntity.class);
			userEntity.setPassword(new BCryptPasswordEncoder().encode("12345"));
			String code = "";
			while (userRepository.findByCheckinCode(code) != null || code.equals("")) {
				code = randomUtils.randCheckinCode();
			}
			userEntity.setCheckinCode(code);
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

	@Override
	public UserDTO findByUsername(String username) {
		return modelMapper.map(userRepository.findByUsername(username), UserDTO.class);
	}

	@Override
	public List<UserDTO> findAllOrderByFullnameASC() {
		List<UserEntity> list = (List<UserEntity>) userRepository.findAllOrderByFullnameASC();
		List<UserDTO> listDTO = new ArrayList<UserDTO>();
		for (UserEntity item : list)
			listDTO.add(modelMapper.map(item, UserDTO.class));
		return listDTO;
	}

	@Override
	public List<UserDTO> findAllOrderByFullnameDESC() {
		List<UserEntity> list = (List<UserEntity>) userRepository.findAllOrderByFullnameDESC();
		List<UserDTO> listDTO = new ArrayList<UserDTO>();
		for (UserEntity item : list)
			listDTO.add(modelMapper.map(item, UserDTO.class));
		return listDTO;
	}

	@Override
	public List<UserDTO> findAllEmployeeHavingRole(String roleName) {
		List<UserEntity> list = (List<UserEntity>) userRepository.findAllEmployeesHavingRole(roleName);
		List<UserDTO> listDTO = new ArrayList<UserDTO>();
		for (UserEntity item : list)
			listDTO.add(modelMapper.map(item, UserDTO.class));
		return listDTO;
	}

}
