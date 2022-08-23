package ducnh.springboot.service;

import ducnh.springboot.model.entity.WorkingHourEntity;
import org.springframework.stereotype.Service;

import ducnh.springboot.dto.WorkingHourDTO;

@Service
public interface IWorkingHourService {
	WorkingHourDTO save(WorkingHourEntity workingHour);

	WorkingHourDTO findByUserId(Long id);
}
