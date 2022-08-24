package ducnh.springboot.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ducnh.springboot.enumForEntity.Status;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "request_working_hour")
@Getter
@Setter
public class RequestWorkingHourEntity extends BaseEntity {
    private LocalTime startMorningTime = LocalTime.of(8, 30);

    private LocalTime endMorningTime = LocalTime.of(12, 00);

    private LocalTime startAfternoonTime = LocalTime.of(13, 00);

    private LocalTime endAfternoonTime = LocalTime.of(17, 30);

    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private Status status = Status.PENDING;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
