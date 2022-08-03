package ducnh.springboot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ducnh.springboot.model.entity.RoleEntity;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long>{
//	@Query(value="SELECT * FROM role WHERE id = :id",nativeQuery = true)
//	Optional<RoleEntity> findById(@Param("id") Long id);
}
