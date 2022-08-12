package ducnh.springboot.specifications;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import ducnh.springboot.model.entity.RoleEntity;
import ducnh.springboot.model.entity.UserEntity;
import ducnh.springboot.utils.DateFormat;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserSpecification<T> implements Specification<T> {
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
	
	public static <T> Specification<T> hasId(Long id) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.<String>get("id"), id);
	}
	
	public static Specification<UserEntity> hasCheckinCode(String code) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.<String>get("checkincode"),code);
	}

	public static Specification<UserEntity> hasFullNameLike(String name) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.<String>get("fullname"), "%" + name + "%");
	}
	
	public static Specification<UserEntity> hasUserName(String name) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.<String>get("username"),name);
	}

	public static Specification<UserEntity> hasRole(String roleName) {
		return (root, query, criterialBuilder) -> {
			Join<UserEntity, RoleEntity> userRole = root.join("roles");
			return criterialBuilder.equal(userRole.get("name"), roleName);
		};
	}

	public static Specification<UserEntity> hasAgeDiff(Boolean greater,int age){
		return (root,query,criterialBuilder) ->{
			SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.y_Md);
			Date now = new Date(System.currentTimeMillis());
			if(greater)
				return criterialBuilder.lessThan(root.get("dateOfBirth"),addDayDate(now, -(age*365)));
			return criterialBuilder.greaterThanOrEqualTo(root.get("dateOfBirth"),addDayDate(now, -(age*365)));
		};
	}

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,
			CriteriaBuilder criteriaBuilder) {
		
		return null;
	}

}
