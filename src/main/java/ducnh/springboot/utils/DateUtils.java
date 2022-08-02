package ducnh.springboot.utils;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.ResourceBundle;

import org.springframework.stereotype.Component;

@Component
public class DateUtils {

	SimpleDateFormat sdf1 = new SimpleDateFormat(DateFormat.y_MdHms);

	ResourceBundle workingTime = ResourceBundle.getBundle("workingtime");

	public Timestamp addDay(Timestamp timestamp,int days) {
		Calendar c = Calendar.getInstance(); 
		c.setTime(timestamp); 
		c.add(Calendar.DATE, days);
		return new Timestamp(c.getTimeInMillis());
	}
	
	public Timestamp parseLDT(LocalDateTime time,String format) throws ParseException {
		SimpleDateFormat sdf2 = new SimpleDateFormat(format);
		return new Timestamp(sdf2.parse(Timestamp.valueOf(time).toString()).getTime());
	}
	
	public String formatDate(Timestamp time, String format) {
		SimpleDateFormat sdf2 = new SimpleDateFormat(format);
		return sdf2.format(time);
	}

	public boolean isSameDay(Date date1, Date date2) {
		if (date1 == null || date2 == null)
			return false;
		return (date1.getYear() == date2.getYear() && date1.getMonth() == date2.getMonth()
				&& date1.getDay() == date2.getDay());
	}

	public int checkinLate(LocalDateTime time) {
		
		String localDateTimeNow = LocalDate.now().toString() + " " + workingTime.getString("startTime");
		long workingDateTimeMiliseconds = new Long(0);
		try {
			workingDateTimeMiliseconds = sdf1.parse(localDateTimeNow).getTime() / 1000 / 60;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ZonedDateTime zdt = ZonedDateTime.of(time, ZoneId.systemDefault());
		long timeNow = zdt.toInstant().toEpochMilli() / 1000 / 60;
		return (int) (timeNow - workingDateTimeMiliseconds);
	}

	public int checkoutEarly(LocalDateTime time) {
		String localDateWorkingTime = LocalDate.now().toString() + " " + workingTime.getString("endTime");
		long workingDateTimeMiliseconds = new Long(0);
		try {
			workingDateTimeMiliseconds = sdf1.parse(localDateWorkingTime).getTime() / 1000 / 60;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ZonedDateTime zdt = ZonedDateTime.of(time, ZoneId.systemDefault());
		long timeNow = zdt.toInstant().toEpochMilli() / 1000 / 60;
		return (int) (workingDateTimeMiliseconds - timeNow);
	}

}
