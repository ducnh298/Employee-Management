package ducnh.springboot.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "role")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RoleEntity extends BaseEntity {
	@Column
	private String name;

	@Column
	private String detail;

	@JsonIgnore
	@ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
	@Fetch(FetchMode.SELECT)
	private List<UserEntity> users = new ArrayList<>();

}
