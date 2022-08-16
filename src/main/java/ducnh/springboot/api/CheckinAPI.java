package ducnh.springboot.api;

import ducnh.springboot.dto.CheckinDTO;
import ducnh.springboot.dto.UserDTO;
import ducnh.springboot.model.entity.CheckinEntity;
import ducnh.springboot.model.entity.UserEntity;
import ducnh.springboot.projection.CheckinsCount;
import ducnh.springboot.service.ICheckinService;
import ducnh.springboot.service.IUserService;
import ducnh.springboot.specifications.UserSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/checkin")
@Secured({ "HR", "STAFF", "INTERN" })
public class CheckinAPI {
	@Autowired
	IUserService userService;

	@Autowired
	ICheckinService checkinService;
	
	@Autowired
	ModelMapper mapper;

	@GetMapping(value = "/{checkinCode}")
	public List<CheckinEntity> getCheckin(@PathVariable String checkinCode) {
		Specification<UserEntity> spec = UserSpecification.hasCheckinCode(checkinCode);
		UserDTO user = userService.findAllHavingSpec(spec,null).getContent().get(0);
		return mapper.map(user, UserEntity.class).getCheckins();
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

	@GetMapping(value = "/find-by-status-dayofweek-resulttime")
	@Secured("HR")
	public Page<CheckinDTO> findByStatusAndDayOfWeekAndResultTime(@RequestBody Map<String ,String> json, @RequestParam int page){
		return checkinService.findByStatusAndDayOfWeekAndResultTime(json, PageRequest.of(page - 1, 5));
	}

	@PostMapping
	@Secured({ "HR", "STAFF", "INTERN" })
	public CheckinDTO checkin(@RequestBody String checkinCode) {
		return checkinService.save(checkinCode);
	}

}
