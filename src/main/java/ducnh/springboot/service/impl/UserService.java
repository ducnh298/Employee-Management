package ducnh.springboot.service.impl;

import ducnh.springboot.converter.UserConverter;
import ducnh.springboot.dto.RoleDTO;
import ducnh.springboot.dto.UserDTO;
import ducnh.springboot.enumForEntity.Provider;
import ducnh.springboot.model.entity.RoleEntity;
import ducnh.springboot.model.entity.UserEntity;
import ducnh.springboot.model.entity.WorkingHourEntity;
import ducnh.springboot.repository.RoleRepository;
import ducnh.springboot.repository.UserRepository;
import ducnh.springboot.repository.WorkingHourRepository;
import ducnh.springboot.service.IRoleService;
import ducnh.springboot.service.IUserService;
import ducnh.springboot.utils.RandomUtils;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Autowired
    RoleRepository roleRepository;

    @Transactional(rollbackOn = Exception.class)
    @Override
    public UserDTO save(@NotNull UserEntity user) {
        UserEntity userEntity;

        if (user.getRoles() != null) {
            user.setRoles(user.getRoles().stream().map(role ->
                roleRepository.findById(role.getId()).orElse(null)).collect(Collectors.toSet()));
        }

        if (user.getId() != null) {
            UserEntity oldUserEntity = userRepository.findById(user.getId()).orElse(null);
            userEntity = converter.toEntity(user, oldUserEntity);
        } else {
            userEntity = modelMapper.map(user, UserEntity.class);
            userEntity.setPassword(new BCryptPasswordEncoder().encode("12345"));
            userEntity.setProvider(Provider.LOCAL);

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
        return modelMapper.map(userEntity, UserDTO.class);
    }

    @Override
    public void delete(Long[] ids) {
        for (Long id : ids)
            userRepository.deleteById(id);
    }

    @Override
    public List<UserDTO> findAll() {
        return converter.toDTOList(userRepository.findAll());
    }

    @Override
    public Page<UserDTO> findAll(Pageable pageable) {
        return converter.toDTOPage(userRepository.findAll(pageable));

    }

    @Override
    public Page<UserDTO> findAll(Specification spec, Pageable pageable) {
        return converter.toDTOPage(userRepository.findAll(spec, pageable));
    }

    @Override
    public UserDTO findById(Long id) {
        return modelMapper.map(userRepository.findById(id), UserDTO.class);
    }

    @Override
    public <T> T findByCheckinCode(Class<T> classtype,String code) {
        return modelMapper.map(userRepository.findByCheckinCode(UserEntity.class, code),classtype);
    }

    @Override
    public UserDTO deleteRoles(Long userId, Long[] roleIds) {
        UserEntity user = userRepository.findById(UserEntity.class,userId);
        Set<RoleEntity> roles = user.getRoles();
        List<RoleEntity> listRole= new ArrayList<>();
        for(Long roleId:roleIds)
            listRole.add(roleRepository.findById(roleId).orElse(null));
        roles.removeAll(listRole);

        user.setRoles(roles);
        userRepository.save(user);

        return modelMapper.map(user,UserDTO.class);
    }

    @Override
    public List<UserDTO> findAllForgetCheckin(Timestamp today, Timestamp tomorrow) {
        return converter.toDTOList(userRepository.findAllForgetCheckin(today,tomorrow));
    }

    @Override
    public List<UserDTO> findAllForgetCheckout(Timestamp today, Timestamp tomorrow) {
        return converter.toDTOList(userRepository.findAllForgetCheckout(today,tomorrow));
    }

    @Override
    public void processOAuthPostLogin(String name,String email) {
        UserEntity existUser = userRepository.findByEmail(UserEntity.class,email);

        if (existUser == null) {
            UserEntity newUser = new UserEntity();
            newUser.setEmail(email);
            newUser.setUsername(email);
            newUser.setFullname(name);
            newUser.setProvider(Provider.GOOGLE);

            newUser.setPassword(new BCryptPasswordEncoder().encode("12345"));

            String code = "";
            while (userRepository.findByCheckinCode(UserEntity.class, code) != null || code.equals("")) {
                code = randomUtils.randCheckinCode();
            }
            newUser.setCheckinCode(code);

            WorkingHourEntity workingHourEntity = new WorkingHourEntity();
            workingHourEntity.setUser(newUser);

            workingHourEntity = workingHourRepository.save(workingHourEntity);

            newUser.setWorkinghour(workingHourEntity);

            userRepository.save(newUser);
        }

    }
}
