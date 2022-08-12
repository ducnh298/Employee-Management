package ducnh.springboot.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ducnh.springboot.model.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {

	public static final String HASH_KEY = "User";

	<T> T findByCheckinCode(Class<T> classtype, String checkinCode);

	<T> T findByUsername(Class<T> classtype, String username);

	<T> List<T> findByFullnameIgnoreCaseContaining(Class<T> classtype, String key);

	<T> T findById(Class<T> classtype, Long Id);

	<T> List<T> findBy(Class<T> classtype, Sort sort);

	List<UserEntity> findAll(Sort sort);

	@Query(value = "SELECT * FROM user AS u WHERE u.id IN (SELECT user_id FROM user_role WHERE role_id IN (SELECT id FROM role WHERE name=:rolename)) ", nativeQuery = true)
	List<UserEntity> findAllEmployeesHavingRole(@Param("rolename") String roleName);
}
