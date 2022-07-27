package ducnh.springboot.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RoleDTO extends BaseDTO {
	private String name;

	private String code;

	private List<UserDTO> users = new ArrayList<UserDTO>();
	
}
