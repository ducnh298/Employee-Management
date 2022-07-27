package ducnh.springboot.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Service;

import ducnh.springboot.dto.CheckinDTO;

@Service
public interface ICheckinService {
	CheckinDTO save(String code);
	List<CheckinDTO> getCheckinsBetweenDates(Timestamp startDate, Timestamp endDate, Long id);
}
