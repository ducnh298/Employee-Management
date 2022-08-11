package ducnh.springboot.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ducnh.springboot.dto.WorkingHourDTO;
import ducnh.springboot.model.entity.UserEntity;
import ducnh.springboot.model.entity.WorkingHourEntity;
import ducnh.springboot.repository.UserRepository;
import ducnh.springboot.repository.WorkingHourRepository;
import ducnh.springboot.service.IWorkingHourService;

@Service
public class WorkingHourService implements IWorkingHourService {

	@Autowired
	ModelMapper mapper;

	@Autowired
	WorkingHourRepository workingHourRepository;

	@Autowired
	UserRepository userRepository;

	@Override
	public WorkingHourDTO save(WorkingHourDTO workingHour) {
		WorkingHourEntity entity = new WorkingHourEntity();

		if (workingHour.getUser() != null) {
			entity = mapper.map(workingHour, WorkingHourEntity.class);
			UserEntity user = userRepository.findById(workingHour.getUser().getId()).orElse(null);
			entity.setUser(user);
			entity.setId(user.getWorkinghour().getId());

			workingHour = mapper.map(workingHourRepository.save(entity), WorkingHourDTO.class);
			return workingHour;
		}
		return null;
	}

}
