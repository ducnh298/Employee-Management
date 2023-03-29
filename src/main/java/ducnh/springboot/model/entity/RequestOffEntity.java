package ducnh.springboot.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import ducnh.springboot.enumForEntity.Status;
import ducnh.springboot.enumForEntity.TimeOff;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import java.sql.Date;

@Entity
@Table(name = "request_off")
@NoArgsConstructor
@Getter
@Setter
@ToString

public class RequestOffEntity extends BaseEntity {
    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent
    @NotBlank(message = "Day off cannot be blank")
    private Date dayOff ;

    @Enumerated(EnumType.STRING)
    private TimeOff timeOff = TimeOff.FULLDAY;

    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private Status status = Status.PENDING;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Fetch(FetchMode.SELECT)
    private UserEntity user;
}
