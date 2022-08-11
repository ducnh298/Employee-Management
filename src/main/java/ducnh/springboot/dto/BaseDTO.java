package ducnh.springboot.dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public abstract class BaseDTO {

	private Long id;

	@JsonIgnore
	private Timestamp createdDate;
	@JsonIgnore
	private String createdBy;
	@JsonIgnore
	private Timestamp modifiedDate;
	@JsonIgnore
	private String modifiedBy;

}
