package ducnh.springboot.utils;

import ducnh.springboot.dto.CheckinDTO;
import ducnh.springboot.dto.UserDTO;
import ducnh.springboot.service.ICheckinService;
import ducnh.springboot.service.IMailService;
import ducnh.springboot.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

@Component
public class Scheduler {
    @Autowired
    IMailService mailService;

    @Autowired
    ICheckinService checkinService;

    @Autowired
    IUserService userService;

    @Autowired
    DateUtils dateUtils;

    SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.dMy);
    SimpleDateFormat sdf2 = new SimpleDateFormat(DateFormat.dMyHms);

    @Scheduled(cron = "0 40 08 ? * MON-FRI")
    @Async
    public void checkinReminder() {
        Timestamp now = null;
        try {
            now = dateUtils.parseLDT(LocalDateTime.now(), DateFormat.y_Md);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Timestamp nowPlus1Day = dateUtils.addDay(now, 1);


// 	Using native query
        List<UserDTO> listEmNotCheckinToday = userService.findAllForgetCheckin(now, nowPlus1Day);
        System.out.println("size: " + listEmNotCheckinToday.size() + "\n");
        for (UserDTO em : listEmNotCheckinToday) {
            StringBuilder content = new StringBuilder("<h1>Hi ");
            content.append(em.getFullname());
            content.append(
                    "!<br> It's 08:30PM <br> Don't forget to checkin when you get to work, or -20.000vnđ!!!</h1>");
            System.out.println(content + "\n");
//            mailService.sendMail(em.getEmail(), "Checkin reminder", content.toString());
        }

    }

    @Scheduled(cron = "0 30 17 ? * MON-FRI")
    @Async
    public void checkoutReminder() {
        Timestamp now = null;
        try {
            now = dateUtils.parseLDT(LocalDateTime.now(), DateFormat.y_Md);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Timestamp nowPlus1Day = dateUtils.addDay(now, 1);
        List<UserDTO> listEmNotCheckoutToday = userService.findAllForgetCheckout(now, nowPlus1Day);
        System.out.println("size: " + listEmNotCheckoutToday.size() + "\n");
        for (UserDTO em : listEmNotCheckoutToday) {
            StringBuilder content = new StringBuilder("<h1>Hi ");
            content.append(em.getFullname());
            content.append(
                    "!<br> It's 17:30PM <br> Don't forget to checkout when you leave work, or -20.000vnđ!!!</h1>");
            System.out.println(content + "\n");
            //  mailService.sendMail(em.getEmail(), "Checkout reminder " + now, content.toString());
        }
    }

    @Scheduled(cron = "00 15 20 ? * SAT")
    @Async
    public void weeklyCheckins() {

        List<UserDTO> listE = userService.findAll(null).getContent();
        LocalDateTime saturday1 = LocalDateTime.now();

        for (UserDTO em : listE) {
            Timestamp saturday = null;
            Timestamp workingDay = null;
            try {
                saturday = dateUtils.parseLDT(saturday1, DateFormat.y_MdHms);
                workingDay = dateUtils.parseLDT(dateUtils.addDay(saturday, -5).toLocalDateTime(), DateFormat.y_MdHms);
            } catch (ParseException e) {

                e.printStackTrace();
            }

            StringBuilder content = new StringBuilder("<h1>Hi ");
            content.append(em.getFullname());
            content.append("!<br> It's Saturday! What a week!<br>");
            content.append("Here's your checkin list and penalties this week.");
            content.append(
                    "<br> If you see any mistakes, please do not hesitate to <a href=\"http://timesheet.nccsoft.vn/app/main/mytimesheets\">complain your PM</a>.</h1><br><br>");

            for (int i = 0; i < 5; i++) {
                Timestamp workingDayPlus1 = dateUtils.addDay(workingDay, 1);
                List<CheckinDTO> checkin1Day = checkinService.getCheckinsBetweenDatesById(workingDay, workingDayPlus1,
                        em.getId());

                if (checkin1Day.size() > 0) {

                    content.append(checkin1Day.get(0).toString() + "<br>");
                    if (checkin1Day.size() > 1)
                        content.append(checkin1Day.get(checkin1Day.size() - 1).toString());
                    else
                        content.append("<font color=red>" + checkin1Day.get(0).getDayOfWeek() + " - "
                                + sdf2.format(checkin1Day.get(0).getCreatedDate())
                                + " You forgot to checkout, penalty = 20.000vnd.</font>");
                } else {
                    content.append("<font color=red>" + sdf.format(workingDay)
                            + " You forgot to checkin and checkout, penalty = 40.000vnd.</font>");
                }
                content.append("<br>-----------------------------------------------<br>");
                workingDay = workingDayPlus1;
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Timestamp.valueOf(saturday1).getTime());

            mailService.sendMail(em.getEmail(),
                    calendar.get(Calendar.WEEK_OF_YEAR) - 1 + "th Weekly Checkin list and penalties: "
                            + sdf.format(dateUtils.addDay(saturday, -5)).substring(0, 2) + " -> "
                            + sdf.format(saturday),
                    content.toString());
        }
    }

}
