package ducnh.springboot.converter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import ducnh.springboot.dto.RoleDTO;
import ducnh.springboot.dto.UserDTO;
import ducnh.springboot.model.entity.RoleEntity;
import ducnh.springboot.model.entity.UserEntity;

@Component
public class UserConverter {

    @Autowired
    ModelMapper mapper;

    public UserEntity toEntity(UserDTO dto, UserEntity old) {
        if (dto.getId() != null)
            old.setId(dto.getId());
        if (dto.getFullname() != null)
            old.setFullname(dto.getFullname());
        if (dto.getDateOfBirth() != null)
            old.setDateOfBirth(dto.getDateOfBirth());
        if (dto.getEmail() != null)
            old.setEmail(dto.getEmail());
        if (dto.getCheckinCode() != null)
            old.setCheckinCode(dto.getCheckinCode());
        if (dto.getRoles() != null) {
            Set<RoleEntity> roles = new HashSet<RoleEntity>();
            if (old.getRoles() != null)
                roles = old.getRoles();

            for (RoleDTO role : dto.getRoles()) {
                roles.add(mapper.map(role, RoleEntity.class));
            }
            old.setRoles(roles);
        }

        return old;
    }

    public List<UserDTO> toDTOList(List<UserEntity> list){
        List<UserDTO> result = new ArrayList<>();
        for(UserEntity entity:list)
            result.add(mapper.map(entity,UserDTO.class));
        return result;
    }

    public Page<UserDTO> toDTOPage(Page<UserEntity> entity) {
        return (Page<UserDTO>) entity.map(new Function<UserEntity, UserDTO>() {
            @Override
            public UserDTO apply(UserEntity userEntity) {
                return mapper.map(userEntity, UserDTO.class);
            }
        });
    }
}
