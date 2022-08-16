package ducnh.springboot.service.impl;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ducnh.springboot.converter.CheckinConverter;
import ducnh.springboot.specifications.FilterSpecification;
import ducnh.springboot.specifications.SearchCriteria;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import ducnh.springboot.dto.CheckinDTO;
import ducnh.springboot.dto.UserDTO;
import ducnh.springboot.model.entity.CheckinEntity;
import ducnh.springboot.model.entity.UserEntity;
import ducnh.springboot.projection.CheckinsCount;
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

	@Autowired
	CheckinConverter checkinConverter;

	public CheckinDTO save(String checkinCode) {
		CheckinDTO checkinDTO = new CheckinDTO();
		LocalDateTime dateTimeNow = LocalDateTime.now();

		UserEntity user = userRepository.findByCheckinCode(UserEntity.class, checkinCode);
		UserDTO userDTO =mapper.map(user, UserDTO.class);
		checkinDTO.setUser(userDTO);

		Timestamp dateNowPlus1;
		List<CheckinDTO> list = new ArrayList<>();
	
			try {
				dateNowPlus1 = dateUtils.addDay(dateUtils.parseLDT(dateTimeNow,DateFormat.y_Md),1);
				list = getCheckinsBetweenDatesById(dateUtils.parseLDT(dateTimeNow,DateFormat.y_Md), dateNowPlus1, user.getId());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		if (list.size()>0) {
			int resultTime ;
				resultTime = dateUtils.checkoutEarly(dateTimeNow,userDTO.getWorkingHour());
			
			checkinDTO.setResultTime(resultTime);
			if (resultTime <= 0)
				checkinDTO.setStatus("checkout ok");
			else
				checkinDTO.setStatus("checkout early");
		} else {
			int resultTime;
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
		List<CheckinDTO> result = new ArrayList<>();
		List<CheckinEntity> entities = checkinRepository.getCheckinsBetweenDatesById(startDate, endDate, id);
		for (CheckinEntity entity : entities)
			result.add(mapper.map(entity, CheckinDTO.class));
		return result;
	}

	@Override
	public List<CheckinDTO> getCheckinsBetweenDates(Timestamp startDate, Timestamp endDate) {
		List<CheckinDTO> result = new ArrayList<>();
		List<CheckinEntity> entities = checkinRepository.getCheckinsBetweenDates(startDate, endDate);
		for (CheckinEntity entity : entities)
			result.add(mapper.map(entity, CheckinDTO.class));
		return result;
	}


	@Override
	public List<CheckinsCount> countCheckinsByUser() {
		
		return checkinRepository.countCheckinsByUser();
	}

	@Override
	public  Page<CheckinDTO> findByStatusAndDayOfWeekAndResultTime(Map<String,String> json, Pageable pageable) {
		FilterSpecification<CheckinEntity> spec1 = new FilterSpecification<>(new SearchCriteria("status","LIKE","%"+json.get("status")+"%"));
		FilterSpecification<CheckinEntity> spec2 = new FilterSpecification<>(new SearchCriteria("dayOfWeek","LIKE",json.get("dayOfWeek")+"%"));
		FilterSpecification<CheckinEntity> spec3 = new FilterSpecification<>(new SearchCriteria("resultTime","GREATER_",json.get("resultTime")));

		return checkinConverter.toDTOPage(checkinRepository.findAll(Specification.where(spec1).and(spec2).and(spec3),pageable));
	}

}
