package ducnh.springboot.model.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
	private List<UserEntity> users = new ArrayList<UserEntity>();

}
