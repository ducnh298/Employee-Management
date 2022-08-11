package ducnh.springboot.specifications;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.persistence.criteria.Join;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import ducnh.springboot.model.entity.RoleEntity;
import ducnh.springboot.model.entity.UserEntity;
import ducnh.springboot.utils.DateFormat;
import ducnh.springboot.utils.DateUtils;

public class UserSpecification {
	public static Date addDayDate(Date date, int days) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, days);
		return new Date(c.getTimeInMillis());
	}
	
	public static Specification<UserEntity> hasFullNameLike(String name) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.<String>get("fullname"), "%" + name + "%");
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
}
