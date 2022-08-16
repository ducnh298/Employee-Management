package ducnh.springboot.specifications;

import ducnh.springboot.model.entity.RoleEntity;
import ducnh.springboot.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import java.sql.Date;
import java.util.Calendar;

@AllArgsConstructor
public class UserSpecification {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3722603606325474025L;

	public static Date addDayDate(Date date, int days) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, days);
		return new Date(c.getTimeInMillis());
	}
	
	public static Specification<UserEntity> hasId(Long id) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id);
	}
	
	public static Specification<UserEntity> hasCheckinCode(String code) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("checkincode"),code);
	}

	public static Specification<UserEntity> hasFullName(String name) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("fullname"), "%" + name + "%");
	}
	
	public static Specification<UserEntity> hasUserName(String name) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("username"),"%"+name+"%");
	}

	public static Specification<UserEntity> hasRole(String roleName) {
		return (root, query, criterialBuilder) -> {
			Join<UserEntity, RoleEntity> userRole = root.join("roles");
			return criterialBuilder.like(userRole.get("name"), roleName+"%%");
		};
	}

	public static Specification<UserEntity> hasAgeDiff(Boolean greater,int age) {
		return (root, query, criterialBuilder) -> {
			Date now = new Date(System.currentTimeMillis());
			if (greater)
				return criterialBuilder.lessThan(root.get("dateOfBirth"), addDayDate(now, -(age * 365)));
			return criterialBuilder.greaterThanOrEqualTo(root.get("dateOfBirth"), addDayDate(now, -(age * 365)));
		};
	}

}
