package ducnh.springboot.controller.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ducnh.springboot.model.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long>{
	UserEntity findByCheckinCode(String checkinCode);
	UserEntity findUserByUsername(String username);
	List<UserEntity> findByFullnameIgnoreCaseContaining(String key);
}
