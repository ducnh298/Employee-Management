package ducnh.springboot.controller;

import ducnh.springboot.dto.CheckinDTO;
import ducnh.springboot.dto.UserDTO;
import ducnh.springboot.projection.CheckinsCount;
import ducnh.springboot.service.ICheckinService;
import ducnh.springboot.service.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/checkin")
@Secured({"HR", "STAFF", "INTERN"})
public class CheckinRestController {
    @Autowired
    IUserService userService;

    @Autowired
    ICheckinService checkinService;

    @Autowired
    ModelMapper mapper;

    @GetMapping()
    @Cacheable("checkin")
    public ResponseEntity<List<CheckinDTO>> getCheckin(@RequestParam String checkinCode) {
        return new ResponseEntity<>(userService.findByCheckinCode(UserDTO.class, checkinCode).getCheckins(), HttpStatus.OK);
    }

    @GetMapping(value = "/find-between-dates")
    @Cacheable("checkin")
    public ResponseEntity<List<CheckinDTO>> getCheckinsBetweenDatesyId(@RequestBody Map<String, Timestamp> date,
                                                                       @RequestParam Long id) {
        return new ResponseEntity<>(checkinService.getCheckinsBetweenDatesById(date.get("startDate"), date.get("endDate"), id), HttpStatus.OK);
    }

    @GetMapping(value = "/count-checkins")
    @Cacheable("checkin")
    public ResponseEntity<List<CheckinsCount>> countCheckinByUser() {
        return new ResponseEntity<>(checkinService.countCheckinsByUser(), HttpStatus.OK);
    }

    @GetMapping(value = "/find-by-status-dayofweek-resulttime")
    @Secured("HR")
    @Cacheable("checkin")
    public ResponseEntity<Page<CheckinDTO>> findByStatusAndDayOfWeekAndResultTime(@RequestBody Map<String, String> json, @RequestParam int page) {
        return new ResponseEntity<>(checkinService.findByStatusAndDayOfWeekAndResultTime(json, PageRequest.of(page - 1, 5)), HttpStatus.OK);
    }

    @PostMapping
    @Secured({"HR", "STAFF", "INTERN"})
    @CachePut("checkin")
    public ResponseEntity<CheckinDTO> checkin(@RequestBody String checkinCode) {
        return new ResponseEntity<>(checkinService.save(checkinCode), HttpStatus.OK);
    }

}
