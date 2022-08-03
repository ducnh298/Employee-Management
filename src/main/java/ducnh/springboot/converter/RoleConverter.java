package ducnh.springboot.converter;

import org.springframework.stereotype.Component;

import ducnh.springboot.dto.RoleDTO;
import ducnh.springboot.model.entity.RoleEntity;

@Component
public class RoleConverter {
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
}
