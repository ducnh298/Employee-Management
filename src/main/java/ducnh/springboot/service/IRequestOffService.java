package ducnh.springboot.service;

import ducnh.springboot.dto.RequestOffDTO;
import ducnh.springboot.enumForEntity.Status;
import ducnh.springboot.model.entity.RequestOffEntity;
import ducnh.springboot.specifications.FilterSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;


public interface IRequestOffService {
    RequestOffDTO save(RequestOffEntity entity);

    List<RequestOffDTO> findAll(Specification<RequestOffEntity> spec);

    List<RequestOffDTO> findByUserId(Long userId);

    List<RequestOffDTO> findMyRequestOff();

    List<RequestOffDTO> updateStatus(Long[] userIds, String status);

}
