package ducnh.springboot.service.impl;

import ducnh.springboot.converter.RequestOffConverter;
import ducnh.springboot.dto.RequestOffDTO;
import ducnh.springboot.enumForEntity.Status;
import ducnh.springboot.model.entity.RequestOffEntity;
import ducnh.springboot.repository.RequestOffRepository;
import ducnh.springboot.repository.UserRepository;
import ducnh.springboot.service.IRequestOffService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class RequestOffService implements IRequestOffService {

    @Autowired
    RequestOffRepository requestOffRepo;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper mapper;

    @Autowired
    RequestOffConverter converter;

    @Override
    public RequestOffDTO save(RequestOffEntity entity) {
        if(entity.getId()==null) {
            entity.setUser(userRepository.findById(entity.getUser().getId()).get());
//            entity.setTimeOff(TimeOff.valueOf(entity.getTimeOff().toString()));
            return mapper.map(requestOffRepo.save(entity), RequestOffDTO.class);
        }
        else {
            RequestOffEntity old = requestOffRepo.findById(entity.getId()).get();
            mapper.map(entity,old);
            return mapper.map(requestOffRepo.save(old),RequestOffDTO.class);
        }
    }

    @Override
    public List<RequestOffDTO> findAllBetween(Timestamp start, Timestamp end) {
        return converter.toDTOList(requestOffRepo.findByCreatedDateBetween(start,end));
    }

    @Override
    public List<RequestOffDTO> findAllByStatus(String status) {
        return converter.toDTOList(requestOffRepo.findByStatus(Status.valueOf(status)));
    }

    @Override
    public List<RequestOffDTO> findAll(Specification<RequestOffEntity> spec) {
        return converter.toDTOList(requestOffRepo.findAll(spec));
    }

    @Override
    public List<RequestOffDTO> updateStatus(Long[] ids, String status) {
        List<RequestOffDTO> result = new ArrayList<>();
        for(Long id:ids){
            RequestOffEntity entity = requestOffRepo.findById(id).get();
            entity.setStatus(Status.valueOf(status));
            result.add(mapper.map(save(entity),RequestOffDTO.class));
        }
        return result;
    }
}
