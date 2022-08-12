package ducnh.springboot.api;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ducnh.springboot.dto.CheckinDTO;
import ducnh.springboot.dto.UserDTO;
import ducnh.springboot.model.entity.CheckinEntity;
import ducnh.springboot.model.entity.UserEntity;
import ducnh.springboot.projection.CheckinsCount;
import ducnh.springboot.service.ICheckinService;
import ducnh.springboot.service.IUserService;
import ducnh.springboot.specifications.UserSpecification;

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

	@PostMapping
	@Secured({ "HR", "STAFF", "INTERN" })
	public CheckinDTO checkin(@RequestBody String checkinCode) {
		return checkinService.save(checkinCode);
	}

}
