package ducnh.springboot.service.impl;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ducnh.springboot.dto.CheckinDTO;
import ducnh.springboot.dto.UserDTO;
import ducnh.springboot.model.entity.CheckinEntity;
import ducnh.springboot.model.entity.UserEntity;
import ducnh.springboot.repository.CheckinRepository;
import ducnh.springboot.repository.UserRepository;
import ducnh.springboot.service.ICheckinService;
import ducnh.springboot.utils.DateUtils;

@Service
public class CheckinService implements ICheckinService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	ModelMapper mapper;

	@Autowired
	CheckinRepository checkinRepository;

	@Autowired
	DateUtils dateUtils;

	public CheckinDTO save(String checkinCode) {
		CheckinDTO checkinDTO = new CheckinDTO();

		UserEntity user = userRepository.findByCheckinCode(checkinCode);
		checkinDTO.setUser(mapper.map(user, UserDTO.class));
		
		List<CheckinEntity> checkins = user.getCheckins();
		CheckinEntity lastCheckin = new CheckinEntity();
		Date lastCheckinDate = Date.valueOf("2021-08-29");

		if (checkins.size() >= 1) {
			lastCheckin = checkins.get(checkins.size() - 1);
			lastCheckinDate = new Date(lastCheckin.getCreatedDate().getTime());
		}

		Date dateNow = new Date(System.currentTimeMillis());
		LocalDateTime dateTimeNow = LocalDateTime.now();
		
		if (dateUtils.isSameDay(dateNow, lastCheckinDate)) {
			int resultTime = dateUtils.checkoutEarly(dateTimeNow);
			checkinDTO.setResultTime(resultTime);
			if (resultTime<=0)
				checkinDTO.setStatus("checkout ok");
			else
				checkinDTO.setStatus("checkout early");
		} else if (!dateUtils.isSameDay(dateNow, lastCheckinDate)) {
			int resultTime = dateUtils.checkinLate(dateTimeNow);
			checkinDTO.setResultTime(resultTime);
			if (resultTime<=15)
				checkinDTO.setStatus("checkin ok");
			else
				checkinDTO.setStatus("checkin late");
		} else {
			checkinDTO.setStatus("unknown");
			checkinDTO.setResultTime(0);
		}
		
		checkinDTO.setDayOfWeek(dateNow.toLocalDate().getDayOfWeek().toString());
		return mapper.map(checkinRepository.save(mapper.map(checkinDTO, CheckinEntity.class)), CheckinDTO.class);

	}

	public List<CheckinDTO> getCheckinsBetweenDates(Timestamp startDate, Timestamp endDate,Long id) {
		List<CheckinDTO> result = new ArrayList<CheckinDTO>();
		List<CheckinEntity> entities = checkinRepository.getCheckinsBetweenDates(startDate, endDate, id);
		for(CheckinEntity entity: entities)
			result.add(mapper.map(entity, CheckinDTO.class));
		return result;
	}

}
