package ducnh.springboot.repository;

import ducnh.springboot.enumForEntity.Status;
import ducnh.springboot.model.entity.CheckinEntity;
import ducnh.springboot.model.entity.RequestOffEntity;
import ducnh.springboot.specifications.FilterSpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface RequestOffRepository extends JpaRepository<RequestOffEntity,Long>, JpaSpecificationExecutor<RequestOffEntity> {

    List<RequestOffEntity> findByCreatedDateBetween(Timestamp start, Timestamp end);

    List<RequestOffEntity> findByStatus(Status status);

    RequestOffEntity findByUserId(Long userId);
}
