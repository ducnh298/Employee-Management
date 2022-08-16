package ducnh.springboot.projection;

import ducnh.springboot.model.entity.RoleEntity;
import lombok.Value;

import java.util.Set;

@Value
public class UserSlimClass {
	Long id;
	String fullname;
	Set<RoleEntity> roles;
}
