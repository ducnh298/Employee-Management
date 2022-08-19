package ducnh.springboot.controller;

import ducnh.springboot.dto.UserDTO;
import ducnh.springboot.dto.WorkingHourDTO;
import ducnh.springboot.model.entity.UserEntity;
import ducnh.springboot.service.IMailService;
import ducnh.springboot.service.IUserService;
import ducnh.springboot.service.IWorkingHourService;
import ducnh.springboot.specifications.UserSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/employee-management")
public class EmployeeRestController {

    @Autowired
    IUserService userService;

    @Autowired
    IMailService mailService;

    @GetMapping(value = "/admin")
    @Secured({"HR"})
    public String adminPage() {
        return "welcome admin";
    }

    @GetMapping("/find/{id}")
    @Cacheable("user")
    public UserDTO findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @GetMapping("/find-all")
    @Cacheable(value = "user", unless = "#result == null")
    public ResponseEntity<Page<UserDTO>> findAll(@RequestParam(value = "orderBy", required = false) Optional<String> orderBy,
                                                 @RequestParam(value = "page") int page,
                                                 @RequestParam(value = "roleName", required = false) Optional<String> roleName,
                                                 @RequestParam(value = "fullName", required = false) Optional<String> fullName,
                                                 @RequestParam(value = "userName", required = false) Optional<String> userName,
                                                 @RequestParam(value = "age", required = false) Optional<String> age) {
        Pageable pageable = PageRequest.of(page - 1, 5);
        Specification<UserEntity> spec = UserSpecification.all();
        if (orderBy.isPresent()) {

            String[] order = orderBy.get().split("-");

            if (order.length > 1) {
                if (order[1].equals("ASC"))
                    pageable = PageRequest.of(page - 1, 5, Sort.by(Sort.Direction.ASC, order[0]));
                else if (order[1].equals("DESC"))
                    pageable = PageRequest.of(page - 1, 5, Sort.by(Sort.Direction.DESC, order[0]));
            } else pageable = PageRequest.of(page - 1, 5, Sort.by(Sort.Direction.DESC, order[0]));
        }
        if (roleName.isPresent())
            spec = spec.and(UserSpecification.hasRole(roleName.get()));

        if (userName.isPresent())
            spec = spec.and(UserSpecification.hasUserName(userName.get()));

        if (age.isPresent()) {
            System.out.println("age diff: " + age);
            String diff = age.get().substring(0, 1);
            int ageE = 0;
            if (diff.equalsIgnoreCase(">") || diff.equalsIgnoreCase("<"))
                ageE = Integer.parseInt(age.get().substring(1));
            else
                ageE = Integer.parseInt(age.get());
            boolean isGreater = true;
            if (diff.equalsIgnoreCase("<"))
                isGreater = false;
            spec = spec.and(UserSpecification.hasAgeDiff(isGreater, ageE));
        }

        return new ResponseEntity<>(userService.findAll(spec, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Secured("HR")
    @CachePut(value = "user")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO user) {
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
//            System.out.println(
                    //mailService.sendMail(user.getEmail(), " NCC's Employee Account Created ", content.toString()));
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Secured("HR")
    @CachePut(value = "user")
    public ResponseEntity<UserDTO> updateEmployee(@RequestBody UserDTO user, @PathVariable Long id) {
        user.setId(id);
        return new ResponseEntity<>(userService.save(user), HttpStatus.OK);
    }

    @DeleteMapping
    @Secured("HR")
    @CacheEvict("user")
    public ResponseEntity<String> deleteEmployees(@RequestBody Long[] ids) {
        userService.delete(ids);
        return new ResponseEntity<>("deleted " + ids.length, HttpStatus.OK);
    }

    @DeleteMapping("/delete-roles")
    @Secured("HR")
    @CacheEvict("user")
    public ResponseEntity<UserDTO> deleteRoles(@RequestParam Long userId,@RequestBody Long[] roleIds) {
        return new ResponseEntity<>(userService.deleteRoles(userId,roleIds), HttpStatus.OK);
    }

}
