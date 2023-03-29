package ducnh.springboot.service.impl;

import ducnh.springboot.converter.RequestOffConverter;
import ducnh.springboot.dto.RequestOffDTO;
import ducnh.springboot.dto.UserDTO;
import ducnh.springboot.enumForEntity.Status;
import ducnh.springboot.model.entity.RequestOffEntity;
import ducnh.springboot.model.entity.UserEntity;
import ducnh.springboot.repository.RequestOffRepository;
import ducnh.springboot.repository.UserRepository;
import ducnh.springboot.service.IRequestOffService;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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

    public void addPropertyMap() {
        mapper.addMappings(new PropertyMap<RequestOffEntity, RequestOffDTO>() {
            @Override
            protected void configure() {
                map().setUser(mapper.map(source.getUser(), UserDTO.class));
            }
        });
    }

    @Override
    public RequestOffDTO save(RequestOffEntity entity) {
        if (entity.getId() != null) {
            RequestOffEntity old = requestOffRepo.findById(entity.getId()).get();
            if (old.getStatus().equals(Status.PENDING)) {
                mapper.map(entity, old);
                return mapper.map(requestOffRepo.save(old), RequestOffDTO.class);
            }
            else return null;
        } else {
            entity.setUser(userRepository.findById(entity.getUser().getId()).get());
//            entity.setTimeOff(TimeOff.valueOf(entity.getTimeOff().toString()));
            return mapper.map(requestOffRepo.save(entity), RequestOffDTO.class);
        }

    }

    @Override
    public List<RequestOffDTO> findAll(Specification<RequestOffEntity> spec) {
        return converter.toDTOList(requestOffRepo.findAll(spec));
    }

    @Override
    public List<RequestOffDTO> findByUserId(Long userId) {
        return converter.toDTOList(requestOffRepo.findByUserId(userId));
    }

    @Override
    public List<RequestOffDTO> findMyRequestOff() {
        UserEntity user = userRepository.findByUsername(UserEntity.class,SecurityContextHolder.getContext().getAuthentication().getName());
        return converter.toDTOList(requestOffRepo.findByUserId(user.getId()));
    }

    @Override
    public List<RequestOffDTO> updateStatus(Long[] ids, String status) {
        List<RequestOffDTO> result = new ArrayList<>();
        for (Long id : ids) {
            RequestOffEntity entity = requestOffRepo.findById(id).get();

            if(!entity.getStatus().equals(Status.CANCEL)||(status.equalsIgnoreCase("CANCEL")&&entity.getStatus().equals(Status.PENDING)))
                entity.setStatus(Status.valueOf(status));
            result.add(mapper.map(requestOffRepo.save(entity), RequestOffDTO.class));
        }
        return result;
    }

}
