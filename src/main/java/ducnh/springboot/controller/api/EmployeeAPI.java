package ducnh.springboot.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ducnh.springboot.controller.service.IUserService;
import ducnh.springboot.model.dto.UserDTO;

@RestController
@RequestMapping("/employee-management")
public class EmployeeAPI {

	@Autowired
	IUserService userService;
	

	@GetMapping(value = "/admin")
	public String adminPage() {
		return "welcome admin";
	}

	@GetMapping
	public List<UserDTO> getUsers() {
		return userService.findAll();
	}

	@GetMapping(value = "/find-by-name")
	public List<UserDTO> findUserByName(@RequestBody String key) {
		return userService.findByFullnameIgnoreCaseContaining(key);
	}
	
	@PostMapping
	public UserDTO createUser(@RequestBody UserDTO user) {
		return userService.save(user);
	}

	@PutMapping(value = "/{id}")
	public UserDTO updateUser(@RequestBody UserDTO user, @PathVariable Long id) {
		user.setId(id);
		return userService.save(user);
	}

	@DeleteMapping
	public String deleteUser(@RequestBody Long[] ids) {
		userService.delete(ids);
		return "deleted " + ids.length;
	}
	

}
