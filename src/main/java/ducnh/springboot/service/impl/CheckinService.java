package ducnh.springboot.service.impl;

import ducnh.springboot.converter.CheckinConverter;
import ducnh.springboot.dto.CheckinDTO;
import ducnh.springboot.dto.UserDTO;
import ducnh.springboot.enumForEntity.Status;
import ducnh.springboot.model.entity.CheckinEntity;
import ducnh.springboot.model.entity.RequestOffEntity;
import ducnh.springboot.model.entity.UserEntity;
import ducnh.springboot.projection.CheckinsCount;
import ducnh.springboot.repository.CheckinRepository;
import ducnh.springboot.repository.RequestOffRepository;
import ducnh.springboot.repository.UserRepository;
import ducnh.springboot.service.ICheckinService;
import ducnh.springboot.specifications.FilterSpecification;
import ducnh.springboot.specifications.SearchCriteria;
import ducnh.springboot.utils.DateFormat;
import ducnh.springboot.utils.DateUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CheckinService implements ICheckinService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	ModelMapper mapper;

	@Autowired
	CheckinRepository checkinRepository;

	@Autowired
	RequestOffRepository requestOffRepository;

	@Autowired
	DateUtils dateUtils;

	@Autowired
	CheckinConverter checkinConverter;

	public void addPropertyMap(){
		mapper.addMappings(new PropertyMap<CheckinEntity, CheckinDTO>() {
			@Override
			protected void configure() {
				map().setUser(mapper.map(source.getUser(), UserDTO.class));
			}
		});
	}

	public CheckinDTO save(String checkinCode) {
		CheckinDTO checkinDTO = new CheckinDTO();
		LocalDateTime dateTimeNow = LocalDateTime.now();
		Timestamp dateNow = null;

		UserEntity user = userRepository.findByCheckinCode(UserEntity.class, checkinCode);
		UserDTO userDTO = mapper.map(user, UserDTO.class);
		checkinDTO.setUser(userDTO);

		Timestamp dateNowPlus1;
		List<CheckinDTO> list = new ArrayList<>();

		Specification<RequestOffEntity> spec1 = new FilterSpecification<>(new SearchCriteria("user", SearchCriteria.Operation.EQUAL, user));
		Specification<RequestOffEntity> spec2 = new FilterSpecification<>(new SearchCriteria("status", SearchCriteria.Operation.EQUAL, Status.APPROVED));
		Specification<RequestOffEntity> spec3 = null;
		Specification<RequestOffEntity> spec4 = null;
	
			try {
				dateNow = 	dateUtils.parseLDT(dateTimeNow,DateFormat.y_Md);

				dateNowPlus1 = dateUtils.addDay(dateNow,1);
				System.out.println("date now plus 1 "+dateNowPlus1);
				list = getCheckinsBetweenDatesById(dateNow, dateNowPlus1, user.getId());

				spec2 = new FilterSpecification<>(new SearchCriteria("dayOff", SearchCriteria.Operation.BETWEEN,
						new SimpleDateFormat(DateFormat.y_Md).parse(dateTimeNow.toString()),
						new SimpleDateFormat(DateFormat.y_Md).parse(dateNowPlus1.toString())));
				System.out.println("timestamp: "+dateNow);
				//spec3 = new FilterSpecification<>(new SearchCriteria("dayOff", SearchCriteria.Operation.LESS, dateNowPlus1));
			} catch (ParseException e) {
				e.printStackTrace();
			}

		List<RequestOffEntity> requestList = requestOffRepository.findAll(spec1.and(spec2).and(spec3).and(spec4));
		RequestOffEntity request = null;
		if(requestList.size()>0)
			request = requestList.get(0);
		if (list.size()>0) {
			int resultTime = dateUtils.checkoutEarly(dateTimeNow,userDTO.getWorkingHour(),request);
			
			checkinDTO.setResultTime(resultTime);
			if (resultTime <= 0)
				checkinDTO.setStatus("checkout ok");
			else
				checkinDTO.setStatus("checkout early");
		} else {
			int resultTime = dateUtils.checkinLate(dateTimeNow,userDTO.getWorkingHour(),request);
			
			
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
	public List<CheckinDTO> findByUserId(Long userId) {
		return checkinConverter.toDTOList(checkinRepository.findByUserId(userId));
	}

	@Override
	public List<CheckinDTO> getCheckinsBetweenDatesById(Timestamp startDate, Timestamp endDate, Long id) {
		List<CheckinEntity> entities = checkinRepository.getCheckinsBetweenDatesById(startDate, endDate, id);
		return checkinConverter.toDTOList(entities);
	}

	@Override
	public List<CheckinDTO> getCheckinsBetweenDates(Timestamp startDate, Timestamp endDate) {
		List<CheckinEntity> entities = checkinRepository.getCheckinsBetweenDates(startDate, endDate);

		return checkinConverter.toDTOList(entities);
	}

	@Override
	public List<CheckinsCount> countCheckinsByUser() {
		
		return checkinRepository.countCheckinsByUser();
	}

	@Override
	public  Page<CheckinDTO> findByStatusAndDayOfWeekAndResultTime(Map<String,String> json, Pageable pageable) {
		FilterSpecification<CheckinEntity> spec1 = new FilterSpecification<>(new SearchCriteria("status", SearchCriteria.Operation.LIKE,"%"+json.get("status")+"%"));
		FilterSpecification<CheckinEntity> spec2 = new FilterSpecification<>(new SearchCriteria("dayOfWeek", SearchCriteria.Operation.LIKE,json.get("dayOfWeek")+"%"));
		FilterSpecification<CheckinEntity> spec3 = new FilterSpecification<>(new SearchCriteria("resultTime", SearchCriteria.Operation.GREATER_,json.get("resultTime")));

		return checkinConverter.toDTOPage(checkinRepository.findAll(Specification.where(spec1).and(spec2).and(spec3),pageable));
	}

}
