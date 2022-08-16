package ducnh.springboot.converter;

import ducnh.springboot.dto.CheckinDTO;
import ducnh.springboot.model.entity.CheckinEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;


@Component
public class CheckinConverter {
    @Autowired
    ModelMapper mapper;

    public Page<CheckinDTO> toDTOPage(Page<CheckinEntity> entity) {
        return entity.map(checkin -> mapper.map(checkin, CheckinDTO.class));
    }
}

