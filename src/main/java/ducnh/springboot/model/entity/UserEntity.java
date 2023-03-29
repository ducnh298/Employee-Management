package ducnh.springboot.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import ducnh.springboot.enumForEntity.Provider;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "user")
@JsonFormat(pattern = "yyyy-MM-dd")
@NoArgsConstructor
@Getter
@Setter

public class UserEntity extends BaseEntity {
    @Column
    @NotBlank(message = "User name cannot be blank")
    private String username;

    @JsonIgnore
    @Column
    @Size(min = 5, max = 20, message
            = "Password must be between 5 and 20 characters")
    private String password;

    @Column
    @NotBlank(message = "Full name cannot be blank")
    private String fullname;

    @Column
    private String checkinCode;

    @Column
    @Past
    private Date dateOfBirth;

    @Column
    @Email(message = "Email should be valid")
    private String email;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    @ManyToMany(fetch = FetchType.EAGER)
    @BatchSize(size = 5)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", targetEntity = CheckinEntity.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CheckinEntity> checkins = new ArrayList<>();

    @OneToMany(mappedBy = "user", targetEntity = RequestOffEntity.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<RequestOffEntity> requestOffs = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    @JsonIgnore
    private WorkingHourEntity workinghour;

    @OneToMany(mappedBy = "user", targetEntity = RequestWorkingHourEntity.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<RequestWorkingHourEntity> requestWorkingHours = new ArrayList<>();

}
