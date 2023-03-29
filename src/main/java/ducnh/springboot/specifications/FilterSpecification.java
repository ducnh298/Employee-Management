package ducnh.springboot.specifications;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class FilterSpecification<T> implements Specification<T> {
    private SearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (criteria.getOperation().equals(SearchCriteria.Operation.BETWEEN)) {
            return criteriaBuilder.between(
                    root.get(criteria.getKey()),(Date)criteria.getValue(),(Date)criteria.getValue2());
        }else if (criteria.getOperation().equals(SearchCriteria.Operation.GREATER_)) {
            return criteriaBuilder.greaterThanOrEqualTo(
                    root.get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equals(SearchCriteria.Operation.GREATER)){
            return criteriaBuilder.greaterThan(
                    root.get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equals(SearchCriteria.Operation.LESS)) {
            return criteriaBuilder.lessThan(
                    root.get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equals(SearchCriteria.Operation.LESS_)) {
            return criteriaBuilder.lessThanOrEqualTo(
                    root.get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equals(SearchCriteria.Operation.EQUAL)) {
            return criteriaBuilder.equal(root.get(criteria.getKey()), criteria.getValue());
        } else if (criteria.getOperation().equals(SearchCriteria.Operation.LIKE)) {
            return criteriaBuilder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
        } 
//        else if (criteria.getOperation().equalsIgnoreCase(SearchCriteria.Operation.JOIN_USER_ROLE)) {
//            Join<UserEntity, RoleEntity> join = root.join("roles");
//            return criteriaBuilder.equal(join.get(criteria.getKey()), criteria.getValue());
//        } else if (criteria.getOperation().equalsIgnoreCase(SearchCriteria.Operation.JOIN_USER_CHECKIN)) {
//            Join<UserEntity, CheckinEntity> join = root.join("checkins");
//            return criteriaBuilder.equal(join.get(criteria.getKey()), criteria.getValue());
//        }
        return null;
    }
}
