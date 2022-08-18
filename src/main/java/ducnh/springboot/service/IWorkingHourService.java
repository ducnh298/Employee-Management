package ducnh.springboot.service;

import org.springframework.stereotype.Service;

import ducnh.springboot.dto.WorkingHourDTO;

@Service
public interface IWorkingHourService {
	WorkingHourDTO save(WorkingHourDTO workingHour);

	WorkingHourDTO findByUserId(Long id);
}
