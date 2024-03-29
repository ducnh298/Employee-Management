package ducnh.springboot.controller;

import ducnh.springboot.CustomException.CheckinException;
import ducnh.springboot.dto.CheckinDTO;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/checkin")
@Secured({"ROLE_HR", "ROLE_STAFF", "ROLE_INTERN"})
public class CheckinRestController {
    @Autowired
    IUserService userService;

    @Autowired
    ICheckinService checkinService;

    @Autowired
    ModelMapper mapper;

    @GetMapping()
    @Cacheable("checkin")
    public ResponseEntity<List<CheckinDTO>> getCheckinByUserId(@RequestParam Long userId) {
        return new ResponseEntity<>(checkinService.findByUserId(userId), HttpStatus.OK);
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
    @Secured("ROLE_HR")
    @Cacheable("checkin")
    public ResponseEntity<Page<CheckinDTO>> findByStatusAndDayOfWeekAndResultTime(@RequestBody Map<String, String> json, @RequestParam int page) {
        return new ResponseEntity<>(checkinService.findByStatusAndDayOfWeekAndResultTime(json, PageRequest.of(page - 1, 5)), HttpStatus.OK);
    }

    @PostMapping
    @Secured({"ROLE_HR", "ROLE_STAFF", "ROLE_INTERN"})
    @CachePut("checkin")
    public ResponseEntity<Object> checkin(@RequestBody String checkinCode, @AuthenticationPrincipal User user) {
        try {
            CheckinDTO result = checkinService.save(checkinCode, user.getUsername());
            if (result != null)
                return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (CheckinException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;
    }

}
