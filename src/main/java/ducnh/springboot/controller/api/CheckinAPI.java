package ducnh.springboot.controller.api;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ducnh.springboot.controller.service.ICheckinService;
import ducnh.springboot.controller.service.IUserService;
import ducnh.springboot.model.dto.CheckinDTO;
import ducnh.springboot.model.dto.UserDTO;

@RestController
@RequestMapping("/checkin")
public class CheckinAPI {
	@Autowired
	IUserService userService;

	@Autowired
	ICheckinService checkinService;

	@GetMapping(value = "/{checkinCode}")
	public List<CheckinDTO> getCheckin(@PathVariable String checkinCode) {
		UserDTO user = userService.findByCheckinCode(checkinCode);
		return user.getCheckins();
	}

	@GetMapping(value = "/{id}/find-between-dates")
	public List<CheckinDTO> getCheckinsBetweenDates(@RequestBody Map<String, Timestamp> date, @PathVariable Long id) {
		return checkinService.getCheckinsBetweenDates(date.get("startDate"), date.get("endDate"), id);
	}

	@PostMapping
	public CheckinDTO checkin(@RequestBody String checkinCode) {
		return checkinService.save(checkinCode);
	}
}
