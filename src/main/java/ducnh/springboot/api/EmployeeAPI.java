package ducnh.springboot.api;

import ducnh.springboot.dto.UserDTO;
import ducnh.springboot.dto.WorkingHourDTO;
import ducnh.springboot.repository.WorkingHourRepository;
import ducnh.springboot.service.IMailService;
import ducnh.springboot.service.IUserService;
import ducnh.springboot.service.IWorkingHourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    @Secured({"HR"})
    public String adminPage() {
        return "welcome admin";
    }

    @GetMapping("/find-by-id/{id}")
    @Cacheable("user")
    public UserDTO findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @GetMapping("/find-all")
    @Cacheable(key = "#root.methodName", value = "user", unless = "#result == null")
    public Page<UserDTO> findAll(@RequestParam int page) {
        return userService.findAll(PageRequest.of(page - 1, 5));
    }

    @GetMapping("/find-all/{orderBy}")
    @Cacheable("user")
    public Page<UserDTO> findAllOrderByFullname(@PathVariable String orderBy, @RequestParam int page) {
        if (orderBy.equalsIgnoreCase("ASC"))
            return userService.findAll( PageRequest.of(page - 1, 5, Sort.by(Sort.Direction.ASC, "fullname")));

        else if (orderBy.equalsIgnoreCase("DESC"))
            return userService.findAll(PageRequest.of(page - 1, 5, Sort.by(Sort.Direction.DESC, "fullname")));

        return null;
    }

    @GetMapping("/find-by-fullname")
    @Cacheable(key = "{#root.methodName,#key}", value = "user", condition = "#key.length >=1", unless = "#result == null")
    public Page<UserDTO> findEmployeesByFullName(@RequestBody String key, @RequestParam int page) {
        return userService.findByFullName(key, PageRequest.of(page - 1, 5));
    }

    @GetMapping("/find-by-username")
    @Cacheable(key = "{#root.methodName,#key}", value = "user", condition = "#key.length >5", unless = "#result == null")
    public Page<UserDTO> findEmployeeByUsername(@RequestBody String key, @RequestParam int page) {
        return userService.findByUserName(key,PageRequest.of(page - 1, 5));
    }

    @GetMapping("/find-all-having-role")
    @Cacheable(key = "{#root.methodName,#json}", value = "user", unless = "#result == null")
    public Page<UserDTO> findEmployeeHavingRole(@RequestBody String roleName, @RequestParam int page) {
        return userService.findByRole(roleName, PageRequest.of(page - 1, 5));
    }

    @GetMapping("/find-workinghour_by_userid/{id}")
    @Cacheable(key = "{#root.methodName,#id}", value = "user", unless = "#result == null")
    public WorkingHourDTO findWorkingHourByUserId(@PathVariable("id") Long id) {
        return userService.findById(id).getWorkingHour();
    }

    @GetMapping("/find-by-fullname-role-agediff")
    @Cacheable(key = "{#root.methodName,#json}", value = "user")
    public Page<UserDTO> findAllByFullnameAndRoleAndAgeDiff(@RequestBody Map<String, String> json, @RequestParam int page) {
        return userService.findAllByFullnameAndRoleAndAgeDiff(json,PageRequest.of(page - 1, 5));
    }

    @PostMapping
    @Secured("HR")
    @CachePut(value = "user")
    public UserDTO createUser(@RequestBody UserDTO user) {
        user = userService.save(user);
        if (user != null) {
            StringBuilder content = new StringBuilder();
            content.append("<h1>Hi ");
            content.append(user.getFullname());
            content.append("!</h1><br><h2>Your NCC's account has been created by: ");
            content.append(user.getCreatedBy());
            content.append(" --- at: ");
            content.append(user.getCreatedDate());
            content.append("<br><br>Your username: ");
            content.append(user.getUsername());
            content.append("<br>Your default password: 12345</h2>");
            content.append("<br>Your working time: </h2>");
            content.append(user.getWorkingHour().toString());
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
