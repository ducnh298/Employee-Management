package ducnh.springboot.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ducnh.springboot.dto.RoleDTO;
import ducnh.springboot.service.IRoleService;

@RestController
@RequestMapping("/role")
public class RoleAPI {
	@Autowired
	IRoleService roleService;
	
	@PostMapping
	@Secured("HR")
	public RoleDTO addRole(@RequestBody RoleDTO role) {
		return roleService.save(role);
	}
	
	@GetMapping("/{id}")
	@Secured("HR")
	public RoleDTO findById(@PathVariable Long id) {
		System.out.println("role id: "+id);
		return roleService.findById(id);
	}
}
