package ducnh.springboot.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
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
        System.out.println("role found by id: " + id + " \n" + roleRepository.findById(id));
        return converter.toDTO(roleRepository.findById(id).orElse(null));
    }

    @Override
    public List<IRoleCount> RoleCountEm() {
//		return null;

        return roleRepository.RoleCountEm();
    }

    @Override
    public Slice<RoleDTO> findByNameContaining(String name, Pageable pageable) {

        return roleRepository.findByNameContaining(name, pageable).map(new Function<RoleEntity, RoleDTO>() {
            @Override
            public RoleDTO apply(RoleEntity roleEntity) {
                return mapper.map(roleEntity,RoleDTO.class);
            }
        });
    }

}
