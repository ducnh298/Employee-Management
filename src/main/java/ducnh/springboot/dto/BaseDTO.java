package ducnh.springboot.dto;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ducnh.springboot.utils.DateFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public abstract class BaseDTO {

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

//	public void setCreatedTime(String createdTime) {
//		this.createdTime = new SimpleDateFormat(DateFormat.dMyHms).format(createdDate);
//	}

//    public String getCreatedTime() {
//        createdTime = new SimpleDateFormat(DateFormat.dMyHms).format(createdDate);
//        return createdTime;
//    }
}
