package ducnh.springboot.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalTime;

@Entity(name = "workinghour")
@Getter
@Setter
@NoArgsConstructor
public class WorkingHourEntity extends BaseEntity {

    @JsonIgnore
    private LocalTime startMorningTime = LocalTime.of(8, 30);

    @JsonIgnore
    private LocalTime endMorningTime = LocalTime.of(12, 00);

    @JsonIgnore
    private LocalTime startAfternoonTime = LocalTime.of(13, 00);

    @JsonIgnore
    private LocalTime endAfternoonTime = LocalTime.of(17, 30);


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="user_id",referencedColumnName = "id")
    @Fetch(FetchMode.SELECT)
    private UserEntity user;
}
