package ducnh.springboot.service.impl;

import ducnh.springboot.converter.RequestWorkingHourConverter;
import ducnh.springboot.dto.RequestWorkingHourDTO;
import ducnh.springboot.dto.UserDTO;
import ducnh.springboot.enumForEntity.Status;
import ducnh.springboot.model.entity.RequestWorkingHourEntity;
import ducnh.springboot.model.entity.UserEntity;
import ducnh.springboot.model.entity.WorkingHourEntity;
import ducnh.springboot.repository.RequestWorkingHourRepository;
import ducnh.springboot.repository.UserRepository;
import ducnh.springboot.repository.WorkingHourRepository;
import ducnh.springboot.service.IRequestWorkingHourService;
import ducnh.springboot.utils.DateFormat;
import ducnh.springboot.utils.DateUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class RequestWorkingHourService implements IRequestWorkingHourService {
    @Autowired
    RequestWorkingHourRepository requestWorkingHourRepo;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper mapper;

    @Autowired
    RequestWorkingHourConverter converter;

    @Autowired
    DateUtils dateUtils;

    public void addPropertyMap() {
//        mapper.addMappings(new PropertyMap<RequestWorkingHourEntity, RequestWorkingHourDTO>() {
//            @Override
//            protected void configure() {
//                map().setUser(mapper.map(source.getUser(), UserDTO.class));
//            }
//        });
    }

    @Override
    public RequestWorkingHourDTO save(RequestWorkingHourEntity entity) {
        addPropertyMap();
        if (entity.getId() != null) {
            RequestWorkingHourEntity old = requestWorkingHourRepo.findById(entity.getId()).get();
            if (old.getStatus().equals(Status.PENDING)) {
                mapper.map(entity, old);
                return mapper.map(requestWorkingHourRepo.save(old), RequestWorkingHourDTO.class);
            } else return null;
        } else {
            entity.setUser(userRepository.findById(entity.getUser().getId()).get());
//            entity.setTimeOff(TimeOff.valueOf(entity.getTimeOff().toString()));
            return mapper.map(requestWorkingHourRepo.save(entity), RequestWorkingHourDTO.class);
        }

    }

    @Override
    public List<RequestWorkingHourDTO> findAll(Specification<RequestWorkingHourEntity> spec) {
        addPropertyMap();
        return converter.toDTOList(requestWorkingHourRepo.findAll(spec));
    }

    @Override
    public List<RequestWorkingHourDTO> findByUserId(Long userId) {
        addPropertyMap();
        return converter.toDTOList(requestWorkingHourRepo.findByUserId(userId));
    }

    @Override
    public List<RequestWorkingHourDTO> findMyRequestWorkingHour() {
        addPropertyMap();
        UserEntity user = userRepository.findByUsername(UserEntity.class, SecurityContextHolder.getContext().getAuthentication().getName());
        return converter.toDTOList(requestWorkingHourRepo.findByUserId(user.getId()));
    }

    @Override
    public List<RequestWorkingHourDTO> updateStatus(Long[] ids, String status) throws ParseException {
        addPropertyMap();
        List<RequestWorkingHourDTO> result = new ArrayList<>();
        for (Long id : ids) {
            RequestWorkingHourEntity entity = requestWorkingHourRepo.findById(id).get();
            if (!entity.getStatus().equals(Status.CANCEL)) {
                entity.setStatus(Status.valueOf(status));
                entity = requestWorkingHourRepo.save(entity);
            }
            result.add(mapper.map(entity, RequestWorkingHourDTO.class));
            if (entity != null && entity.getStatus().equals(Status.APPROVED)) {
                Timestamp today = new Timestamp(System.currentTimeMillis());
//                if (entity.getApplyDate() == null || entity.getApplyDate().compareTo(today) < 0) {
//                   entity.setApplyDate(new Date(new SimpleDateFormat(DateFormat.y_Md).parse(dateUtils.addDay(today,1).toString()).getTime()));
//                   requestWorkingHourRepo.save(entity);
//                }
            }
        }
        return result;
    }

}
