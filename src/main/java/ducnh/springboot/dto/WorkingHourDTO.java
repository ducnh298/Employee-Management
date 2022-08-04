package ducnh.springboot.dto;

import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.OneToOne;

import ducnh.springboot.model.entity.UserEntity;
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
