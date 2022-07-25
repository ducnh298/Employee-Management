package ducnh.springboot.model.dto;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDTO extends BaseDTO {

	private String fullname;

	private String checkinCode;

	private Date dateOfBirth;

	private String email;

	private String mainRole;

	@Setter(AccessLevel.NONE)
	private List<CheckinDTO> checkins = new ArrayList<CheckinDTO>();

	@Setter(AccessLevel.NONE)
	private Set<RoleDTO> roles = new HashSet<RoleDTO>();

	public void setCheckins(List<CheckinDTO> checkins) {
		if (checkins != null)
			for (CheckinDTO item : checkins) {
				item.setUser(null);
				item.setCheckinCode(checkinCode);
			}
		this.checkins = checkins;
	}

	public void setRoles(Set<RoleDTO> roles) {
		if (roles != null)
			for (RoleDTO item : roles) {
				item.setUsers(null);
			}
		this.roles = roles;
	}

}
