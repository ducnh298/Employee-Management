package ducnh.springboot.dto;

import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ducnh.springboot.utils.DateFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckinDTO extends BaseDTO {

	@JsonIgnore
	SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.dMyHms);
	
	private String dayOfWeek;
	
	@Setter(AccessLevel.NONE)
	@Getter(AccessLevel.NONE)
	private String checkinTime;

	private String status;

	private int resultTime;

	private String checkinCode;

	private UserDTO user;
	
	public CheckinDTO() {
		super();
	}

	
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

	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.dMyHms);
		SimpleDateFormat sdf2 = new SimpleDateFormat(DateFormat.dMyHms);
		StringBuilder str = new StringBuilder("");
		if (resultTime > 15)
			str.append("<font color=red>");
		str.append(dayOfWeek);
		str.append(" - ");
		str.append(sdf.format(this.getCreatedDate()) );
		str.append(",  ");
		str.append(status+" ");
		str.append(resultTime);
		str.append(" min ");
		if (resultTime > 15) {
			str.append(" ==> penalty = 20.000vnd");
			str.append("</font>");
		}
		return str.toString();
	}

	public String getCheckinTime() {
		checkinTime = sdf.format(getCreatedDate());
		return checkinTime;
	}

	public void setCheckinTime() {
		checkinTime = sdf.format(getCreatedDate());
	}


	
}
