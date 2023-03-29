package ducnh.springboot.service;

import ducnh.springboot.dto.CheckinDTO;
import ducnh.springboot.projection.CheckinsCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;


public interface ICheckinService {
    CheckinDTO save(String code, String username) throws Exception;

    List<CheckinDTO> findByUserId(Long userId);

    List<CheckinDTO> getCheckinsBetweenDatesById(Timestamp startDate, Timestamp endDate, Long id);

    List<CheckinDTO> getCheckinsBetweenDates(Timestamp startDate, Timestamp endDate);

    List<CheckinsCount> countCheckinsByUser();

    Page<CheckinDTO> findByStatusAndDayOfWeekAndResultTime(Map<String, String> json, Pageable pageable);


}
