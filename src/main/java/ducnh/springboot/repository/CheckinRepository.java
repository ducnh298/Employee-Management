package ducnh.springboot.repository;

import ducnh.springboot.model.entity.CheckinEntity;
import ducnh.springboot.projection.CheckinsCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface CheckinRepository extends JpaRepository<CheckinEntity, Long>, JpaSpecificationExecutor<CheckinEntity> {
	@Query("FROM checkin t WHERE createdDate BETWEEN :startDate AND :endDate AND user_id = :id")
	 List<CheckinEntity> getCheckinsBetweenDatesById(@Param("startDate") Timestamp startDate,
			@Param("endDate") Timestamp endDate, @Param("id") Long id);

	@Query("FROM checkin t WHERE createdDate BETWEEN :startDate AND :endDate ")
	 List<CheckinEntity> getCheckinsBetweenDates(@Param("startDate") Timestamp startDate,
			@Param("endDate") Timestamp endDate);

	@Query("SELECT new ducnh.springboot.projection.CheckinsCount(c.dayOfWeek,COUNT(c.id)) FROM checkin AS c GROUP BY c.dayOfWeek")
	 List<CheckinsCount> countCheckinsByUser();

	List<CheckinEntity> findByUserId(Long userId);
}
