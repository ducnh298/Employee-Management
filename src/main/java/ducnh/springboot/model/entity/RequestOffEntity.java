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
import java.sql.Timestamp;

@Entity
@Table(name = "request_off")
@NoArgsConstructor
@Getter
@Setter
@ToString

public class RequestOffEntity extends BaseEntity {
    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp dayOff;
    @Enumerated(EnumType.STRING)
    private TimeOff timeOff;

    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private Status status = Status.PENDING;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Fetch(FetchMode.SELECT)
    private UserEntity user;
}
