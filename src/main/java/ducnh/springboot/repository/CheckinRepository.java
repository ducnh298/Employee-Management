package ducnh.springboot.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ducnh.springboot.model.entity.CheckinEntity;

@Repository
public interface CheckinRepository extends JpaRepository<CheckinEntity, Long>{
	@Query(value = "from checkin t where createdDate BETWEEN :startDate AND :endDate AND user_id = :id")
	public List<CheckinEntity> getCheckinsBetweenDates(@Param("startDate") Timestamp startDate,@Param("endDate") Timestamp endDate, @Param("id") Long id);
}
