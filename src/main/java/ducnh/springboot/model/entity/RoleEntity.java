package ducnh.springboot.model.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter; 

@Entity(name="role")
@NoArgsConstructor
@Getter
@Setter
public class RoleEntity extends BaseEntity{
	@Column
	private String name;
	
	@Column
	private String detail;
	
	@ManyToMany(mappedBy = "roles")
	private List<UserEntity> users = new ArrayList<UserEntity>();	
	
}
