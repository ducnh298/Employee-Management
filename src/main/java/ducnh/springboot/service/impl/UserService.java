package ducnh.springboot.service.impl;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.jetbrains.annotations.NotNull;
import org.modelmapper.Converter;
import org.modelmapper.Converters;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
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
	public UserDTO save(@NotNull UserDTO user) {
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
			workingHourEntity.setUser(userEntity);

			workingHourEntity = workingHourRepository.save(workingHourEntity);

			userEntity.setWorkinghour(workingHourEntity);
		}

		userEntity = userRepository.save(userEntity);
		user = modelMapper.map(userEntity, UserDTO.class);
		return user;
	}

	@Override
	public void delete(Long[] ids) {
		for (Long id : ids)
			userRepository.deleteById(id);
	}

	@Override
	public Page<UserDTO> findAllHavingSpec(Specification<UserEntity> spec,Pageable pageable) {
			Page<UserEntity> entity = userRepository.findAll(spec,pageable);
			return (Page<UserDTO>) entity.map(new Function<UserEntity, UserDTO>() {
				@Override
				public UserDTO apply(UserEntity userEntity) {
					return modelMapper.map(userEntity,UserDTO.class);
				}
			});
	}


}
