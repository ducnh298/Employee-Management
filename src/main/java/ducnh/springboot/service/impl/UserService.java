package ducnh.springboot.service.impl;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import ducnh.springboot.converter.UserConverter;
import ducnh.springboot.dto.UserDTO;
import ducnh.springboot.model.entity.UserEntity;
import ducnh.springboot.model.entity.WorkingHourEntity;
import ducnh.springboot.repository.UserRepository;
import ducnh.springboot.repository.WorkingHourRepository;
import ducnh.springboot.service.IRoleService;
import ducnh.springboot.service.IUserService;
import ducnh.springboot.utils.RandomUtils;

@Service
//@PropertySource("classpath:workingtime.properties")
public class UserService implements IUserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	WorkingHourRepository workingHourRepository;

	@Autowired
	IRoleService roleService;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	UserConverter converter;

	@Autowired
	RandomUtils randomUtils;
//	
//	@Value("${startMorningTime}")
//	private String startMorTime;
//	public LocalTime startMorningTime = LocalTime.parse(startMorTime);
//	
//	@Value("${endMorningTime}")
//	private String endMorTime;
//	public LocalTime endMorningTime = LocalTime.parse(endMorTime);
//	
//	@Value("${startAfternoonTime}")
//	private String startAfTime;
//	public LocalTime startAfternoonTime= LocalTime.parse(startAfTime);
//	
//	@Value("${endAfternoonTime}")
//	private String endAfTime;
//	public LocalTime endAfternoonTime = LocalTime.parse(endAfTime);

	@Transactional(rollbackOn = Exception.class)
	@Override
	public UserDTO save(UserDTO user) {
		UserEntity userEntity = new UserEntity();

		if (user.getRoles() != null) {
			user.setRoles(user.getRoles().stream().map(role -> {
				return roleService.findById(role.getId());
			}).collect(Collectors.toSet()));
		}

		if (user.getId() != null) {
			UserEntity oldUserEntity = userRepository.findById(UserEntity.class, user.getId());
			userEntity = converter.toEntity(user, oldUserEntity);
		} else {
			userEntity = modelMapper.map(user, UserEntity.class);
			userEntity.setPassword(new BCryptPasswordEncoder().encode("12345"));

			String code = "";
			while (userRepository.findByCheckinCode(UserEntity.class, code) != null || code.equals("")) {
				code = randomUtils.randCheckinCode();
			}
			userEntity.setCheckinCode(code);

			WorkingHourEntity workingHourEntity = new WorkingHourEntity();
			workingHourEntity.setStartMorningTime(LocalTime.of(8, 30));
			workingHourEntity.setEndMorningTime(LocalTime.of(12, 00));
			workingHourEntity.setStartAfternoonTime(LocalTime.of(13, 00));
			workingHourEntity.setEndAfternoonTime(LocalTime.of(17, 30));
			workingHourEntity.setUser(userEntity);

			workingHourEntity = workingHourRepository.save(workingHourEntity);

			userEntity.setWorkinghour(workingHourEntity);
		}

		userEntity = userRepository.save(userEntity);
		user = modelMapper.map(userEntity, UserDTO.class);
		return user;
	}

	@Override
	public <T> List<T> findAll(Class<T> classtype) {
		if (classtype.equals(UserDTO.class)) {
			List<UserEntity> list = userRepository.findAll();
			List<T> listDTO = new ArrayList<T>();
			for (UserEntity item : list)
				listDTO.add(modelMapper.map(item, classtype));
			return (List<T>) listDTO;
		}
		return (List<T>) userRepository.findBy(classtype, null);
	}

	@Override
	public void delete(Long[] ids) {
		for (Long id : ids)
			userRepository.deleteById(id);
	}

	@Override
	public <T> T findById(Class<T> classtype, Long id) {
		if (classtype.equals(UserDTO.class)) {
			UserEntity entity = userRepository.findById(UserEntity.class, id);
			UserDTO user = modelMapper.map(entity, UserDTO.class);

			return (T) user;
		}
		return userRepository.findById(classtype, id);
	}

	@Override
	public <T> T findByCheckinCode(Class<T> classtype, String checkinCode) {
		if (classtype.equals(UserDTO.class)) {
			try {
				return (T) modelMapper.map(userRepository.findByCheckinCode(UserEntity.class, checkinCode), classtype);
			} catch (NullPointerException e) {
				System.out.println(e.getMessage());
			}
			return null;
		}
		return userRepository.findByCheckinCode(classtype, checkinCode);
	}

	@Override
	public <T> List<T> findByFullnameIgnoreCaseContaining(Class<T> classtype, String key) {
		if (classtype.equals(UserDTO.class)) {
			List<UserEntity> entities = userRepository.findByFullnameIgnoreCaseContaining(UserEntity.class, key);
			List<UserDTO> result = new ArrayList<UserDTO>();
			for (UserEntity entity : entities) {
				result.add(modelMapper.map(entity, UserDTO.class));
			}
			return (List<T>) result;
		}
		return userRepository.findByFullnameIgnoreCaseContaining(classtype, key);
	}

	@Override
	public <T> T findByUsername(Class<T> classtype, String username) {
		if (classtype.equals(UserDTO.class)) {
			return (T) modelMapper.map(userRepository.findByUsername(UserEntity.class, username), UserDTO.class);
		}
		return userRepository.findByUsername(classtype, username);
	}

	@Override
	public <T> List<T> findAllOrderByFullnameASC(Class<T> classtype) {
		if (classtype.equals(UserDTO.class)) {

			List<UserEntity> list = userRepository.findBy(UserEntity.class, Sort.by("fullname"));
			List<UserDTO> listDTO = new ArrayList<UserDTO>();
			for (UserEntity item : list)
				listDTO.add(modelMapper.map(item, UserDTO.class));
			return (List<T>) listDTO;
		}
		return (List<T>) userRepository.findBy(classtype, Sort.by("fullname"));
	}

	@Override
	public <T> List<T> findAllOrderByFullnameDESC(Class<T> classtype) {

		if (classtype.equals(UserDTO.class)) {
			List<UserEntity> list = userRepository.findBy(UserEntity.class, Sort.by("fullname").descending());
			List<UserDTO> listDTO = new ArrayList<UserDTO>();
			for (UserEntity item : list)
				listDTO.add(modelMapper.map(item, UserDTO.class));
			return (List<T>) listDTO;
		} else
			return userRepository.findBy(classtype, Sort.by("fullname").descending());
	}

	@Override
	public List<UserDTO> findAllEmployeeHavingRole(String roleName) {
		List<UserEntity> list = userRepository.findAllEmployeesHavingRole(roleName);
		List<UserDTO> listDTO = new ArrayList<UserDTO>();
		for (UserEntity item : list)
			listDTO.add(modelMapper.map(item, UserDTO.class));
		return listDTO;
	}

	@Override
	public List<UserDTO> findAllHavingSpecifications(Specification<UserEntity> spec) {

		List<UserEntity> list = userRepository.findAll(spec);
		List<UserDTO> listDTO = new ArrayList<UserDTO>();
		for (UserEntity item : list)
			listDTO.add(modelMapper.map(item, UserDTO.class));
		return listDTO;
	}

}
