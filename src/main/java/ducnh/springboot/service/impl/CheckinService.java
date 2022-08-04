package ducnh.springboot.service.impl;

import java.sql.Timestamp;
import java.text.ParseException;
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
import ducnh.springboot.utils.DateFormat;
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
		LocalDateTime dateTimeNow = LocalDateTime.now();

		UserEntity user = userRepository.findByCheckinCode(checkinCode);
		UserDTO userDTO =mapper.map(user, UserDTO.class);
		checkinDTO.setUser(userDTO);

		Timestamp dateNowPlus1 = null;
		List<CheckinDTO> list = new ArrayList<CheckinDTO>();
	
			try {
				dateNowPlus1 = dateUtils.addDay(dateUtils.parseLDT(dateTimeNow,DateFormat.y_Md),1);
				list = getCheckinsBetweenDatesById(dateUtils.parseLDT(dateTimeNow,DateFormat.y_Md), dateNowPlus1, user.getId());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		if (list.size()>0) {
			int resultTime = 0;
				resultTime = dateUtils.checkoutEarly(dateTimeNow,userDTO.getWorkingHour());
			
			checkinDTO.setResultTime(resultTime);
			if (resultTime <= 0)
				checkinDTO.setStatus("checkout ok");
			else
				checkinDTO.setStatus("checkout early");
		} else {
			int resultTime = 0;
				resultTime = dateUtils.checkinLate(dateTimeNow,userDTO.getWorkingHour());
			
			
			checkinDTO.setResultTime(resultTime);
			if (resultTime <= 15)
				checkinDTO.setStatus("checkin ok");
			else
				checkinDTO.setStatus("checkin late");
		}
		
		checkinDTO.setDayOfWeek(dateTimeNow.toLocalDate().getDayOfWeek().toString());
		return mapper.map(checkinRepository.save(mapper.map(checkinDTO, CheckinEntity.class)), CheckinDTO.class);

	}


	@Override
	public List<CheckinDTO> getCheckinsBetweenDatesById(Timestamp startDate, Timestamp endDate, Long id) {
		List<CheckinDTO> result = new ArrayList<CheckinDTO>();
		List<CheckinEntity> entities = checkinRepository.getCheckinsBetweenDatesById(startDate, endDate, id);
		for (CheckinEntity entity : entities)
			result.add(mapper.map(entity, CheckinDTO.class));
		return result;
	}

	@Override
	public List<CheckinDTO> getCheckinsBetweenDates(Timestamp startDate, Timestamp endDate) {
		List<CheckinDTO> result = new ArrayList<CheckinDTO>();
		List<CheckinEntity> entities = checkinRepository.getCheckinsBetweenDates(startDate, endDate);
		for (CheckinEntity entity : entities)
			result.add(mapper.map(entity, CheckinDTO.class));
		return result;
	}

}
