package ducnh.springboot.converter;

import ducnh.springboot.dto.RoleDTO;
import ducnh.springboot.model.entity.RoleEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class RoleConverter {
	@Autowired
	ModelMapper mapper;
	public RoleDTO toDTO(RoleEntity entity) {
		RoleDTO dto= new RoleDTO();
		
		dto.setId(entity.getId());
		dto.setCreatedDate(entity.getCreatedDate());
		dto.setCreatedBy(entity.getCreatedBy());
		dto.setModifiedDate(entity.getModifiedDate());
		dto.setModifiedBy(entity.getModifiedBy());
		dto.setName(entity.getName());
		dto.setDetail(entity.getDetail());
		
		return dto;
	}

	public Set<RoleDTO> toDTOSet(Set<RoleEntity> list){
		Set<RoleDTO> result = new HashSet<>();
		for(RoleEntity role:list)
			result.add(mapper.map(role,RoleDTO.class));
		return result;
	}
}
