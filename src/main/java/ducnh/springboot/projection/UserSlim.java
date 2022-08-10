package ducnh.springboot.projection;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ducnh.springboot.model.entity.RoleEntity;

public interface UserSlim {

	@JsonIgnore
	public Long getId();

	@JsonIgnore
	public String getCheckinCode();

	@JsonIgnore
	public String getUsername();

	@JsonIgnore
	public Set<RoleEntity> getRoles();
	
	public default String getUserCode() {
		StringBuilder str =  new StringBuilder(getUsername()+"(");
		str.append(getId().toString()+"."+getCheckinCode()+")[ ");
		
		for(RoleEntity role:getRoles())
			str.append(role.getName()+" ");
		str.append("]");
		return str.toString();
	}

	public String getFullname();

	public Date getDateOfBirth();

	public String getEmail();

	

}
