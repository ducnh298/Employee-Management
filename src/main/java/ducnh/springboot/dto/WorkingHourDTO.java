package ducnh.springboot.dto;

import java.time.LocalTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WorkingHourDTO extends BaseDTO {

	private LocalTime startMorningTime;

	private LocalTime endMorningTime;

	private LocalTime startAfternoonTime;

	private LocalTime endAfternoonTime;

	private UserDTO user;
}
