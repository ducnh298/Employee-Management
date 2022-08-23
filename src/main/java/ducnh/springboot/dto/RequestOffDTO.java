package ducnh.springboot.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import ducnh.springboot.enumForEntity.Status;
import ducnh.springboot.enumForEntity.TimeOff;
import ducnh.springboot.model.entity.UserEntity;
import ducnh.springboot.utils.DateFormat;
import lombok.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestOffDTO extends BaseDTO {

    @JsonIgnore
    private Timestamp dayOff;

    @Setter(AccessLevel.NONE)
    private String dayRequestOff;
    private TimeOff timeOff;
    private Status status;


    private UserEntity user;

    public void setDayRequestOff(String dayRequestOff) throws ParseException {
        this.dayRequestOff = new SimpleDateFormat(DateFormat.dMyHms).format(dayOff);
    }
}
