package ducnh.springboot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ducnh.springboot.model.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long>{
	UserEntity findByCheckinCode(String checkinCode);
	UserEntity findByUsername(String username);
	List<UserEntity> findByFullnameIgnoreCaseContaining(String key);
	Optional<UserEntity> findById(Long Id);
	
	@Query("FROM user u ORDER BY u.fullname ASC")
	List<UserEntity> findAllOrderByFullnameASC();
	
	@Query("FROM user u ORDER BY u.fullname DESC")
	List<UserEntity> findAllOrderByFullnameDESC();
	
	@Query(value="SELECT * FROM user AS u WHERE u.id IN (SELECT user_id FROM user_role WHERE role_id IN (SELECT id FROM role WHERE name=:rolename)) ",nativeQuery = true)
	List<UserEntity> findAllEmployeesHavingRole(@Param("rolename") String roleName);
}
