package ducnh.springboot.converter;

import ducnh.springboot.dto.RequestOffDTO;
import ducnh.springboot.model.entity.RequestOffEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RequestOffConverter {

    @Autowired
    ModelMapper mapper;

    public List<RequestOffDTO> toDTOList(List<RequestOffEntity> list){
        List<RequestOffDTO> result = new ArrayList<>();
        for(RequestOffEntity entity:list)
            result.add(mapper.map(entity,RequestOffDTO.class));
        return result;
    }
}
