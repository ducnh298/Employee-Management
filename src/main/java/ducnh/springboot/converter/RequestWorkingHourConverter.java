package ducnh.springboot.converter;

import ducnh.springboot.dto.RequestWorkingHourDTO;
import ducnh.springboot.model.entity.RequestWorkingHourEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RequestWorkingHourConverter {

    @Autowired
    ModelMapper mapper;

    public List<RequestWorkingHourDTO> toDTOList(List<RequestWorkingHourEntity> list){
        List<RequestWorkingHourDTO> result = new ArrayList<>();
        for(RequestWorkingHourEntity entity:list)
            result.add(mapper.map(entity,RequestWorkingHourDTO.class));
        return result;
    }
}
