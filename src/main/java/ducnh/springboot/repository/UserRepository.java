package ducnh.springboot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ducnh.springboot.model.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long>{
	UserEntity findByCheckinCode(String checkinCode);
	UserEntity findByUsername(String username);
	List<UserEntity> findByFullnameIgnoreCaseContaining(String key);
	Optional<UserEntity> findById(Long Id);
}
