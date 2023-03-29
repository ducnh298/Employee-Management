package ducnh.springboot.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Setter
public abstract class BaseDTO {

    @JsonIgnore
    private Long id;

    @Getter(AccessLevel.NONE)
    @JsonIgnore
    private String createdTime;

    @JsonIgnore
    private Timestamp createdDate;

    @JsonIgnore
    private String createdBy;

    @JsonIgnore
    private Timestamp modifiedDate;

    @JsonIgnore
    private String modifiedBy;
}
