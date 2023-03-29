package ducnh.springboot.converter;

import ducnh.springboot.dto.CheckinDTO;
import ducnh.springboot.model.entity.CheckinEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class CheckinConverter {
    @Autowired
    ModelMapper mapper;

    public Page<CheckinDTO> toDTOPage(Page<CheckinEntity> entity) {
        return entity.map(checkin -> mapper.map(checkin, CheckinDTO.class));
    }
    public List<CheckinDTO> toDTOList(List<CheckinEntity> list){
        List<CheckinDTO> result= new ArrayList<>();
        for(CheckinEntity checkin: list)
            result.add(mapper.map(checkin,CheckinDTO.class));
        return  result;
    }
}

