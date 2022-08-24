package ducnh.springboot.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import ducnh.springboot.enumForEntity.Status;
import ducnh.springboot.enumForEntity.TimeOff;
import ducnh.springboot.model.entity.UserEntity;
import ducnh.springboot.utils.DateFormat;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestWorkingHourDTO extends BaseDTO {

    private LocalTime startMorningTime = LocalTime.of(8, 30);

    private LocalTime endMorningTime = LocalTime.of(12, 00);

    private LocalTime startAfternoonTime = LocalTime.of(13, 00);

    private LocalTime endAfternoonTime = LocalTime.of(17, 30);

    private Status status = Status.PENDING;

    @JsonIgnore
    private UserDTO user;
}
