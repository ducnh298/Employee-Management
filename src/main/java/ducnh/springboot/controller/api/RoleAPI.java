package ducnh.springboot.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ducnh.springboot.controller.service.IRoleService;
import ducnh.springboot.model.dto.RoleDTO;

@RestController
@RequestMapping("/role")
public class RoleAPI {
	@Autowired
	IRoleService roleService;
	
	@PostMapping
	public RoleDTO addRole(@RequestBody RoleDTO role) {
		return roleService.save(role);
	}
}
