package ducnh.springboot.converter;

import java.util.HashSet;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

}
