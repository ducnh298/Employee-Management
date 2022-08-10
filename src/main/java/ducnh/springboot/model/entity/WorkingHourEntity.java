package ducnh.springboot.model.entity;

import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "workinghour")
@Getter
@Setter
@NoArgsConstructor
public class WorkingHourEntity extends BaseEntity {
	@Column
	private LocalTime startMorningTime;
	@Column
	private LocalTime endMorningTime;
	@Column
	private LocalTime startAfternoonTime;
	@Column
	private LocalTime endAfternoonTime;

	@OneToOne(mappedBy = "workinghour")
	private UserEntity user;
}
