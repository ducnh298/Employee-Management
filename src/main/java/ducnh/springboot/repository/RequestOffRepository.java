package ducnh.springboot.repository;

import ducnh.springboot.model.entity.RequestOffEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestOffRepository extends JpaRepository<RequestOffEntity,Long>, JpaSpecificationExecutor<RequestOffEntity> {
        List<RequestOffEntity> findByUserId(Long userId);
}
