package ducnh.springboot.model.entity;

import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "workinghour")
@Getter
@Setter
@NoArgsConstructor
public class WorkingHourEntity extends BaseEntity {
    @Column
    private LocalTime startMorningTime = LocalTime.of(8, 30);
    @Column
    private LocalTime endMorningTime = LocalTime.of(12, 00);
    @Column
    private LocalTime startAfternoonTime = LocalTime.of(13, 00);
    @Column
    private LocalTime endAfternoonTime = LocalTime.of(17, 30);

    @OneToOne(mappedBy = "workinghour")
    @Fetch(FetchMode.SELECT)
    private UserEntity user;
}
