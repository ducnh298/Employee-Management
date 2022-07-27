package ducnh.springboot.dto;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public abstract class BaseDTO {
	private Long id;

	private Timestamp createdDate;

	private String createdBy;

	private Timestamp modifiedDate;

	private String modifiedBy;
	
}
