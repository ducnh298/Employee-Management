package ducnh.springboot.repository;

import ducnh.springboot.model.entity.RequestWorkingHourEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface RequestWorkingHourRepository extends JpaRepository<RequestWorkingHourEntity,Long>, JpaSpecificationExecutor<RequestWorkingHourEntity> {
    List<RequestWorkingHourEntity> findByUserId(Long userId);
}
