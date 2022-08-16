package ducnh.springboot.repository;

import ducnh.springboot.model.entity.RoleEntity;
import ducnh.springboot.projection.IRoleCount;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

//	<T> Page<T> findAll(FilterSpecification<T> spec, Pageable pageable, Class<T> classtype);

	Slice<RoleEntity> findByNameContaining(String name,Pageable pageable);

	@Query(value = "SELECT r.name AS roleName,COUNT(u.id) AS count FROM role AS r, user AS u, user_role AS ur WHERE r.id = ur.role_id AND u.id = ur.user_id GROUP BY r.name ", nativeQuery = true)
	List<IRoleCount> RoleCountEm();

}
