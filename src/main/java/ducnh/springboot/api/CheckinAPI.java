package ducnh.springboot.api;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ducnh.springboot.dto.CheckinDTO;
import ducnh.springboot.dto.UserDTO;
import ducnh.springboot.projection.CheckinsCount;
import ducnh.springboot.service.ICheckinService;
import ducnh.springboot.service.IUserService;

@RestController
@RequestMapping("/checkin")
@Secured({ "HR", "STAFF", "INTERN" })
public class CheckinAPI {
	@Autowired
	IUserService userService;

	@Autowired
	ICheckinService checkinService;

	@GetMapping(value = "/{checkinCode}")
	public List<CheckinDTO> getCheckin(@PathVariable String checkinCode) {
		UserDTO user = userService.findByCheckinCode(UserDTO.class, checkinCode);
		return user.getCheckins();
	}

	@GetMapping(value = "/{id}/find-between-dates")
	public List<CheckinDTO> getCheckinsBetweenDatesyId(@RequestBody Map<String, Timestamp> date,
			@PathVariable Long id) {
		return checkinService.getCheckinsBetweenDatesById(date.get("startDate"), date.get("endDate"), id);
	}

	@GetMapping(value = "/count-checkins")
	public List<CheckinsCount> countCheckinByUser() {
		return checkinService.countCheckinsByUser();
	}

	@PostMapping
	@Secured({ "HR", "STAFF", "INTERN" })
	public CheckinDTO checkin(@RequestBody String checkinCode) {
		return checkinService.save(checkinCode);
	}

}
