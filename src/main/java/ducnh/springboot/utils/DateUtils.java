package ducnh.springboot.utils;

import ducnh.springboot.dto.WorkingHourDTO;
import ducnh.springboot.enumForEntity.TimeOff;
import ducnh.springboot.model.entity.RequestOffEntity;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;

@Component
public class DateUtils {

    public Timestamp addDay(Timestamp timestamp, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(timestamp);
        c.add(Calendar.DATE, days);
        return new Timestamp(c.getTimeInMillis());
    }

    public LocalDateTime addDay(LocalDateTime time, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(Date.from(time.atZone(ZoneId.systemDefault()).toInstant()));
        c.add(Calendar.DATE, days);
        return LocalDateTime.ofInstant(c.toInstant(), ZoneId.systemDefault());
    }

    public Timestamp parseLDT(LocalDateTime time, String format) throws ParseException {
        return new Timestamp(new SimpleDateFormat(format).parse(Timestamp.valueOf(time).toString()).getTime());
    }

    public String formatDate(Timestamp time, String format) {
        return new SimpleDateFormat(format).format(time);
    }

    public boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null)
            return false;
        return (date1.getYear() == date2.getYear() && date1.getMonth() == date2.getMonth()
                && date1.getDay() == date2.getDay());
    }

    public int checkinLate(LocalDateTime time, WorkingHourDTO woDto, RequestOffEntity request) {
        String localDateTimeNow = LocalDate.now() + " " + woDto.getStartMorningTime() + ":00";
        if (request != null) {
            if (request.getTimeOff().equals(TimeOff.FULLDAY))
                return 0;
            if (request.getTimeOff().equals(TimeOff.MORNING))
                localDateTimeNow = LocalDate.now() + " 13:00:00";
        }

        long workingDateTimeMiliseconds = 0;
        try {
            workingDateTimeMiliseconds = new SimpleDateFormat(DateFormat.y_MdHms).parse(localDateTimeNow).getTime() / 1000 / 60;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ZonedDateTime zdt = ZonedDateTime.of(time, ZoneId.systemDefault());
        long timeNow = zdt.toInstant().toEpochMilli() / 1000 / 60;
        return (int) (timeNow - workingDateTimeMiliseconds);
    }

    public int checkoutEarly(LocalDateTime time, WorkingHourDTO woDto, RequestOffEntity request) {
        String localDateTimeNow = LocalDate.now() + " " + woDto.getEndAfternoonTime() + ":00";

        if (request != null) {
            if (request.getTimeOff().equals(TimeOff.FULLDAY))
                return 0;
            if (request.getTimeOff().equals(TimeOff.AFTERNOON))
                localDateTimeNow = LocalDate.now() + " 12:00:00";
        }

        long workingDateTimeMiliseconds = 0;
        try {
            workingDateTimeMiliseconds = new SimpleDateFormat(DateFormat.y_MdHms).parse(localDateTimeNow).getTime() / 1000 / 60;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ZonedDateTime zdt = ZonedDateTime.of(time, ZoneId.systemDefault());
        long timeNow = zdt.toInstant().toEpochMilli() / 1000 / 60;
        return (int) (workingDateTimeMiliseconds - timeNow);
    }
}
