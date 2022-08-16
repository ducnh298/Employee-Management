package ducnh.springboot.service.impl;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import ducnh.springboot.specifications.UserSpecification;
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
            UserEntity oldUserEntity = userRepository.findById(user.getId()).orElse(null);
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
    public Page<UserDTO> findAll(Pageable pageable) {
        return converter.toDTOPage(userRepository.findAll(pageable));

    }

    @Override
    public UserDTO findById(Long id) {
        return modelMapper.map(userRepository.findById(id), UserDTO.class);
    }

    @Override
    public UserDTO findByCheckinCode(String code, Pageable pageable) {
        return modelMapper.map(userRepository.findByCheckinCode(UserEntity.class, code), UserDTO.class);
    }

    @Override
    public Page<UserDTO> findAllHavingSpec(Specification<UserEntity> spec, Pageable pageable) {
        return converter.toDTOPage(userRepository.findAll(spec, pageable));
    }

    @Override
    public Page<UserDTO> findByFullName(String key, Pageable pageable) {
        Specification<UserEntity> spec = UserSpecification.hasFullName(key);
        return converter.toDTOPage(userRepository.findAll(spec, pageable));
    }

    @Override
    public Page<UserDTO> findByUserName(String key, Pageable pageable) {
        Specification<UserEntity> spec = UserSpecification.hasUserName(key);
        return converter.toDTOPage(userRepository.findAll(spec, pageable));
    }

    @Override
    public Page<UserDTO> findByRole(String rolename, Pageable pageable) {
        Specification<UserEntity> spec = UserSpecification.hasRole(rolename);
        return converter.toDTOPage(userRepository.findAll(spec, pageable));
    }

    @Override
    public Page<UserDTO> findAllByFullnameAndRoleAndAgeDiff(Map<String, String> json, Pageable pageable) {
        Specification<UserEntity> spec = UserSpecification.hasFullName(json.get("name").toString())
                .and(UserSpecification.hasRole(json.get("roleName").toString()).and(UserSpecification
                        .hasAgeDiff(Boolean.parseBoolean(json.get("greater")), Integer.parseInt(json.get("age").toString()))));

        return converter.toDTOPage(userRepository.findAll(spec, pageable));
    }


}
