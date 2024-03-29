package ducnh.springboot.service.impl;

import ducnh.springboot.converter.RoleConverter;
import ducnh.springboot.converter.UserConverter;
import ducnh.springboot.dto.UserDTO;
import ducnh.springboot.dto.WorkingHourDTO;
import ducnh.springboot.enumForEntity.Provider;
import ducnh.springboot.model.entity.RoleEntity;
import ducnh.springboot.model.entity.UserEntity;
import ducnh.springboot.model.entity.WorkingHourEntity;
import ducnh.springboot.repository.RoleRepository;
import ducnh.springboot.repository.UserRepository;
import ducnh.springboot.repository.WorkingHourRepository;
import ducnh.springboot.service.IUserService;
import ducnh.springboot.utils.RandomUtils;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
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
public class UserService implements IUserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WorkingHourRepository workingHourRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UserConverter converter;

    @Autowired
    RoleConverter roleConverter;

    @Autowired
    RandomUtils randomUtils;

    @Autowired
    RoleRepository roleRepository;

    public void addPropertyMap() {
        modelMapper.addMappings(new PropertyMap<UserEntity, UserDTO>() {
            @Override
            protected void configure() {
                map().setWorkingHour(modelMapper.map(source.getWorkinghour(), WorkingHourDTO.class));
                map().setRoles(roleConverter.toDTOSet(source.getRoles()));
            }
        });
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    @CacheEvict(value = "user", allEntries = true)
    public UserDTO save(@NotNull UserEntity user) {
        UserEntity userEntity = null;

        if (user.getRoles() != null) {
            user.setRoles(user.getRoles().stream().map(role ->
                    roleRepository.findById(role.getId()).get()).collect(Collectors.toSet()));
        }

        if (user.getId() != null) {
            UserEntity oldUserEntity = userRepository.findById(user.getId()).get();
            userEntity = converter.toEntity(user, oldUserEntity);
        } else {

            if (userRepository.findByUsername(UserEntity.class, user.getUsername()) != null
                    && userRepository.findByEmail(UserEntity.class, user.getEmail()) != null) {
                userEntity = user;

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
        }
        if(userEntity!=null) {
            userEntity = userRepository.save(userEntity);
            return modelMapper.map(userEntity, UserDTO.class);
        }
        else return null;
    }

    @Override
    @CacheEvict(value = "user", allEntries = true)
    public void delete(Long[] ids) {
        for (Long id : ids)
            userRepository.deleteById(id);
    }

    @Override
    @Cacheable("user")
    public List<UserDTO> findAll() {
        return converter.toDTOList(userRepository.findAll());
    }

    @Override
    @Cacheable("user")
    public Page<UserDTO> findAll(Pageable pageable) {
        return converter.toDTOPage(userRepository.findAll(pageable));
    }

    @Override
    @Cacheable(value = "user")
    public Page<UserDTO> findAll(Specification spec, Pageable pageable) {
        return converter.toDTOPage(userRepository.findAll(spec, pageable));
    }

    @Override
    @Cacheable(value = "user")
    public UserDTO findById(Long id) {
        return modelMapper.map(userRepository.findById(id).get(), UserDTO.class);
    }

    @Override
    @Cacheable(value = "user")
    public <T> T findByCheckinCode(Class<T> classtype, String code) {
        return modelMapper.map(userRepository.findByCheckinCode(UserEntity.class, code), classtype);
    }

    @Override
    @CachePut("user")
    public UserDTO deleteRoles(Long userId, Long[] roleIds) {
        UserEntity user = userRepository.findById(UserEntity.class, userId);
        Set<RoleEntity> roles = user.getRoles();
        List<RoleEntity> listRole = new ArrayList<>();
        for (Long roleId : roleIds)
            listRole.add(roleRepository.findById(roleId).get());
        roles.removeAll(listRole);

        user.setRoles(roles);
        userRepository.save(user);

        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    @Cacheable("user")
    public List<UserDTO> findAllForgetCheckin(Timestamp today, Timestamp tomorrow) {
        return converter.toDTOList(userRepository.findAllForgetCheckin(today, tomorrow));
    }

    @Override
    @Cacheable("user")
    public List<UserDTO> findAllForgetCheckout(Timestamp today, Timestamp tomorrow) {
        return converter.toDTOList(userRepository.findAllForgetCheckout(today, tomorrow));
    }

    @Override
    @CacheEvict(value = "user", allEntries = true)
    public void processOAuthPostLogin(String name, String email) {
        UserEntity existUser = userRepository.findByEmail(UserEntity.class, email);

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
