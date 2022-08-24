package ducnh.springboot.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@ToString
@AllArgsConstructor
public class UserDTO extends BaseDTO {

	private String fullname;

	private String username;

	private String checkinCode;

	private Date dateOfBirth;

	private String email;

	private Set<RoleDTO> roles = new HashSet<>();

	@JsonIgnore
	private WorkingHourDTO workingHour;

	@JsonIgnore
	private List<RequestOffDTO> requestsOff;

	@JsonIgnore
	private List<CheckinDTO> checkins;

}
