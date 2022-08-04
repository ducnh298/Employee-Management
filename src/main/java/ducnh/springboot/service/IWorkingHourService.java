package ducnh.springboot.service;

import org.springframework.stereotype.Service;

import ducnh.springboot.dto.WorkingHourDTO;

@Service
public interface IWorkingHourService {
	public WorkingHourDTO save(WorkingHourDTO workingHour);
}
