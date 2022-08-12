package ducnh.springboot.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ducnh.springboot.dto.UserDTO;
import ducnh.springboot.dto.WorkingHourDTO;
import ducnh.springboot.model.entity.UserEntity;
import ducnh.springboot.projection.UserSlim;
import ducnh.springboot.repository.WorkingHourRepository;
import ducnh.springboot.service.IMailService;
import ducnh.springboot.service.IUserService;
import ducnh.springboot.service.IWorkingHourService;
import ducnh.springboot.specifications.UserSpecification;

@RestController
@RequestMapping("/employee-management")
public class EmployeeAPI {

	@Autowired
	IUserService userService;

	@Autowired
	IWorkingHourService workingHourService;

	@Autowired
	IMailService mailService;

	@Autowired
	WorkingHourRepository workingHourRepository;

	@GetMapping(value = "/admin")
	@Secured({ "HR" })
	public String adminPage() {
		return "welcome admin";
	}

	@GetMapping("/find-by-id/{id}")
	@Cacheable("user")
	public UserDTO getEmployee(@PathVariable Long id) {
		Specification<UserEntity> spec = UserSpecification.hasId(id);
		return userService.findAllHavingSpec(spec,null).getContent().get(0);
	}
	
	@GetMapping("/find-all/page{page}")
	@Cacheable(key="#root.methodName",value = "user",unless="#result == null")
	public Page<UserDTO> getEmployees(@PathVariable int page) {
		return (userService.findAllHavingSpec(null,PageRequest.of(page-1, 5)));
	}

	@GetMapping("/find-all/{orderBy}/page{page}")
	@Cacheable("user")
	public Page<UserDTO> getEmployeesOrderByFullname(@PathVariable String orderBy,@PathVariable int page) {
		if (orderBy.equalsIgnoreCase("ASC")) 
			return (userService.findAllHavingSpec(null,PageRequest.of(page-1, 5, Sort.by(Sort.Direction.DESC, "fullname"))));

		else if (orderBy.equalsIgnoreCase("DESC")) 
			return (userService.findAllHavingSpec(null,PageRequest.of(page-1, 5, Sort.by(Sort.Direction.DESC, "fullname"))));

		return null;
	}

	@GetMapping("/find-by-name/page{page}")
	@Cacheable(key = "{#root.methodName,#key}", value = "user", condition = "#key.length <5",unless="#result == null")
	public Page<UserDTO> findEmployeesByName(@RequestBody String key,@PathVariable int page) {
		Specification<UserEntity> spec = UserSpecification.hasFullNameLike("a");

		return  userService.findAllHavingSpec( spec,PageRequest.of(page-1, 5));
	}

	@GetMapping("/find-by-user-name")
	@Cacheable(key="{#root.methodName,#username}",value = "user",condition = "#username.length >5",unless="#result == null")
	public UserSlim findEmployeeByUsername(@RequestBody String username) {
		Specification<UserEntity> spec = UserSpecification.hasUserName(username);

		return (UserSlim) userService.findAllHavingSpec( spec,null).getContent().get(0);
	}

	@GetMapping("/find-all-having-role/page{page}")
	@Cacheable(key="{#root.methodName,#json}",value = "user",unless="#result == null")
	public Page<UserDTO> findEmployeeHavingRole(@RequestBody Map<String, String> json,@PathVariable int page) {
		Specification<UserEntity> spec = UserSpecification.hasRole(json.get("roleName").toString());

		return (userService.findAllHavingSpec( spec,PageRequest.of(page-1, 5)));
	}

	@GetMapping("/find-workinghour_by_userid/{id}")
	@Cacheable(key="{#root.methodName,#id}",value = "user",unless="#result == null")
	public WorkingHourDTO findWorkingHourByUserId(@PathVariable("id") Long id) {
		Specification<UserEntity> spec = UserSpecification.hasId(id);
		return userService.findAllHavingSpec(spec,null).getContent().get(0).getWorkingHour();
	}

	@GetMapping("/find-all-having-fullname-like-and-role/page{page}")
	@Cacheable(key="{#root.methodName,#json}",value = "user")
	public Page<UserDTO> findEmployeeHavingFullnameLikeAndRole(@RequestBody Map<String, Object> json,@PathVariable int page) {
		Specification<UserEntity> spec = UserSpecification.hasFullNameLike(json.get("name").toString())
				.and(UserSpecification.hasRole(json.get("roleName").toString()).and(UserSpecification
						.hasAgeDiff((Boolean) json.get("greater"), Integer.parseInt(json.get("age").toString()))));

		return (userService.findAllHavingSpec( spec,PageRequest.of(page-1, 5)));
	}

	@PostMapping
	@Secured("HR")
	@CachePut(value = "user")
	public UserDTO createUser(@RequestBody UserDTO user) {
		user = userService.save(user);
		if (user != null) {
			StringBuilder content = new StringBuilder("");
			content.append("<h1>Hi ");
			content.append(user.getFullname());
			content.append("!</h1><br><h2>Your NCC's account has been created by: ");
			content.append(user.getCreatedBy());
			content.append(" --- at: ");
			content.append(user.getCreatedDate());
			content.append("<br><br>Your username: ");
			content.append(user.getUsername());
			content.append("<br>Your default password: 12345</h2>");
			System.out.println(
					mailService.sendMail(user.getEmail(), " NCC's Employee Account Created ", content.toString()));
		}
		return user;
	}

	@PutMapping("/{id}")
	@Secured("HR")
	@CachePut(value = "user")
	public UserDTO updateEmployee(@RequestBody UserDTO user, @PathVariable Long id) {
		user.setId(id);
		return userService.save(user);
	}

	@DeleteMapping
	@Secured("HR")
	@CacheEvict("user")
	public String deleteEmployees(@RequestBody Long[] ids) {
		userService.delete(ids);
		return "deleted " + ids.length;
	}

	@PostMapping("/{id}/set-working-hour")
	@CachePut(value = "user")
	@Secured("HR")
	public WorkingHourDTO setWorkingHour(@PathVariable("id") Long id, @RequestBody WorkingHourDTO workingHour) {
		UserDTO user = new UserDTO();
		user.setId(id);
		workingHour.setUser(user);
		return workingHourService.save(workingHour);
	}

}
