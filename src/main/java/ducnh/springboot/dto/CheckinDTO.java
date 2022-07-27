package ducnh.springboot.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter

public class CheckinDTO extends BaseDTO {

	private String dayOfWeek;

	private String status;

	private int resultTime;
	
	private String checkinCode;

	private UserDTO user;

	public String getCheckinCode() {
		if (user != null)
			return user.getCheckinCode();
		return checkinCode;
	}

	public void setCheckinCode(String checkinCode) {
		if (user != null)
			this.checkinCode = user.getCheckinCode();
		else
			this.checkinCode = checkinCode;
	}

}
