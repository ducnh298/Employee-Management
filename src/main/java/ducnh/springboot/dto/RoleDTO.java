package ducnh.springboot.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RoleDTO extends BaseDTO {
	private String name;

	private String detail;

	@JsonIgnore
	private List<UserDTO> users = new ArrayList<UserDTO>();
	
}
