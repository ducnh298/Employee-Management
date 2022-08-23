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
	public WorkingHourDTO save(WorkingHourEntity entity) {

		if (entity.getUser() != null) {
			UserEntity user = userRepository.findById(entity.getUser().getId()).get();
			entity.setUser(user);
			entity.setId(user.getWorkinghour().getId());

			return mapper.map(workingHourRepository.save(entity), WorkingHourDTO.class);
		}

		return null;
	}

	public WorkingHourDTO findByUserId(Long id){
		return mapper.map(workingHourRepository.findByUserId(id), WorkingHourDTO.class);
	}

}
