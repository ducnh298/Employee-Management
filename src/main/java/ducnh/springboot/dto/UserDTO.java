package ducnh.springboot.dto;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserDTO extends BaseDTO {

	private String fullname;
	
	private String username;

	private String checkinCode;

	private Date dateOfBirth;

	private String email;

	@JsonIgnore	
	private List<CheckinDTO> checkins = new ArrayList<CheckinDTO>();

	private Set<RoleDTO> roles = new HashSet<RoleDTO>();

}
