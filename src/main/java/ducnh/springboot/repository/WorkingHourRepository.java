package ducnh.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ducnh.springboot.model.entity.WorkingHourEntity;

@Repository
public interface WorkingHourRepository extends JpaRepository<WorkingHourEntity, Long> {
//	@Query(value="SELECT workinghour.id,start_morning_time,end_morning_time,start_afternoon_time,start_afternoon_time,workinghour.created_date,workinghour.created_by,workinghour.modified_date,workinghour.modified_by FROM workinghour WHERE workinghour.id = (SELECT workinghour_id FROM user WHERE id =:user_id)",nativeQuery = true )
//	WorkingHourEntity findByUserId(@Param("user_id") Long user_id);

    WorkingHourEntity findByUserId(Long userId);
}
