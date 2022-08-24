package ducnh.springboot.service;

import ducnh.springboot.dto.RequestWorkingHourDTO;
import ducnh.springboot.model.entity.RequestWorkingHourEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface IRequestWorkingHourService {
    RequestWorkingHourDTO save(RequestWorkingHourEntity entity);

    List<RequestWorkingHourDTO> findAll(Specification<RequestWorkingHourEntity> spec);

    List<RequestWorkingHourDTO> findByUserId(Long userId);

    List<RequestWorkingHourDTO> findMyRequestWorkingHour();

    List<RequestWorkingHourDTO> updateStatus(Long[] userIds, String status);
}
