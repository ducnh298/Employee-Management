package ducnh.springboot.repository;

import ducnh.springboot.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {

//	public static final String HASH_KEY = "User";

	<T> T findByCheckinCode(Class<T> classtype, String checkinCode);

	<T> T findByUsername(Class<T> classtype, String username);

	<T> T findByFullname(Class<T> classtype, String username);

	<T> T findById(Class<T> classtype, Long Id);

	@Query(value = "SELECT * FROM user AS u WHERE u.id IN (SELECT user_id FROM user_role WHERE role_id IN (SELECT id FROM role WHERE name=:rolename)) ", nativeQuery = true)
	List<UserEntity> findAllEmployeesHavingRole(@Param("rolename") String roleName);
}
