package ducnh.springboot.controller;

import ducnh.springboot.dto.WorkingHourDTO;
import ducnh.springboot.model.entity.UserEntity;
import ducnh.springboot.model.entity.WorkingHourEntity;
import ducnh.springboot.service.IWorkingHourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/working-hour")
public class WorkingHourRestController {
    @Autowired
    IWorkingHourService workingHourService;

    @GetMapping("/find")
    @Cacheable(value = "workinghour")
    public ResponseEntity<WorkingHourDTO> findWorkingHourByUserId(@RequestParam Long userId) {
        return new ResponseEntity<>(workingHourService.findByUserId(userId), HttpStatus.OK);
    }

    @PostMapping("/set")
    @CachePut(value = "workinghour")
    @Secured("ROLE_HR")
    public ResponseEntity<WorkingHourDTO> setWorkingHour(@RequestParam("id") Long id, @RequestBody WorkingHourEntity workingHour) {
        UserEntity user = new UserEntity();
        user.setId(id);
        workingHour.setUser(user);
        return new ResponseEntity<>(workingHourService.save(workingHour), HttpStatus.OK);
    }

}
