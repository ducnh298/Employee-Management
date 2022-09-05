package ducnh.springboot.utils;

import ducnh.springboot.dto.WorkingHourDTO;
import ducnh.springboot.enumForEntity.Status;
import ducnh.springboot.enumForEntity.TimeOff;
import ducnh.springboot.model.entity.*;
import ducnh.springboot.repository.*;
import ducnh.springboot.service.IMailService;
import ducnh.springboot.specifications.FilterSpecification;
import ducnh.springboot.specifications.SearchCriteria;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Component
public class Scheduler {
    @Autowired
    IMailService mailService;

    @Autowired
    CheckinRepository checkinRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    RequestOffRepository requestOffRepo;

    @Autowired
    RequestWorkingHourRepository requestWorkingHourRepo;

    @Autowired
    WorkingHourRepository workingHourRepo;

    @Autowired
    DateUtils dateUtils;

    @Autowired
    ModelMapper mapper;

    SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.dMy);

    @Scheduled(cron = "0 40 08 ? * MON-FRI")
    @Async
    public void checkinReminder() throws ParseException {
        Timestamp now = null;
        try {
            now = dateUtils.parseLDT(LocalDateTime.now(), DateFormat.y_Md);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Timestamp nowPlus1Day = dateUtils.addDay(now, 1);


// 	Using native query
        List<UserEntity> listEmNotCheckinToday = userRepo.findAllForgetCheckin(now, nowPlus1Day);
        System.out.println("size: " + listEmNotCheckinToday.size() + "\n");
        for (UserEntity em : listEmNotCheckinToday) {
            StringBuilder content = new StringBuilder("<h1>Hi ");
            content.append(em.getFullname());
            content.append(
                    "!<br> It's 08:30PM <br> Don't forget to checkin when you get to work, or -20.000vnđ!!!</h1>");
            System.out.println(content + "\n");
//            mailService.sendMail(em.getEmail(), "Checkin reminder"+sdf.parse(String.valueOf(now)), content.toString());
        }

    }

    @Scheduled(cron = "0 45 17 ? * MON-FRI")
    @Async
    public void checkoutReminder() throws ParseException {
        Timestamp now = null;
        try {
            now = dateUtils.parseLDT(LocalDateTime.now(), DateFormat.y_Md);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Timestamp nowPlus1Day = dateUtils.addDay(now, 1);
        List<UserEntity> listEmNotCheckoutToday = userRepo.findAllForgetCheckout(now, nowPlus1Day);
        System.out.println("size: " + listEmNotCheckoutToday.size() + "\n");
        for (UserEntity em : listEmNotCheckoutToday) {
            StringBuilder content = new StringBuilder("<h1>Hi ");
            content.append(em.getFullname());
            content.append(
                    "!<br> It's 17:30PM <br> Don't forget to checkout when you leave work, or -20.000vnđ!!!</h1>");
            System.out.println(content + "\n");
//              mailService.sendMail(em.getEmail(), "Checkout reminder " + sdf.parse(String.valueOf(now)), content.toString());
        }
    }

    @Scheduled(cron = "20 54 10 ? * THU")
    @Async
    public void weeklyCheckins() {

        List<UserEntity> listE = userRepo.findAll();
        LocalDateTime saturdayLDT = LocalDateTime.now();

        for (UserEntity em : listE) {
            Timestamp saturday = null;
            Timestamp workingDay = null;
            try {
                saturday = dateUtils.parseLDT(saturdayLDT, DateFormat.y_Md);
                workingDay = dateUtils.parseLDT(dateUtils.addDay(saturday, -5).toLocalDateTime(), DateFormat.y_Md);
            } catch (ParseException e) {

                e.printStackTrace();
            }

            StringBuilder content = new StringBuilder("<h1>Hi ");
            content.append(em.getFullname());
            content.append("!<br> It's Saturday! What a week!<br>");
            content.append("Here's your checkin list and penalties this week.");
            content.append(
                    "<br> If you see any mistakes, please do not hesitate to <a href=\"http://timesheet.nccsoft.vn/app/main/mytimesheets\">complain your PM</a>.</h1><br><br>");
            int countPen = 0;
            for (int i = 0; i < 5; i++) {


                content.append(workingDay.toLocalDateTime().toLocalDate().getDayOfWeek().toString() + " " + sdf.format(workingDay) + ": <br>");

                Timestamp workingDayPlus1 = dateUtils.addDay(workingDay, 1);
                List<CheckinEntity> checkin1Day = checkinRepo.getCheckinsBetweenDatesById(workingDay, workingDayPlus1,
                        em.getId());
                Specification<RequestOffEntity> spec = new FilterSpecification<>(new SearchCriteria("user", SearchCriteria.Operation.EQUAL, mapper.map(em, UserEntity.class)));
                spec = spec.and(new FilterSpecification<>(new SearchCriteria("status", SearchCriteria.Operation.EQUAL, Status.APPROVED)));
                try {
                    spec = spec.and(new FilterSpecification<>(new SearchCriteria("dayOff", SearchCriteria.Operation.BETWEEN,
                            new SimpleDateFormat(DateFormat.y_Md).parse(workingDay.toString()),
                            new SimpleDateFormat(DateFormat.y_Md).parse(workingDayPlus1.toString()))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Optional<RequestOffEntity> oRequestOff = requestOffRepo.findAll(spec).stream()
                        .filter(request -> request.getStatus().equals(Status.APPROVED)).findFirst();
                RequestOffEntity requestOff = null;
                if (oRequestOff.isPresent())
                    requestOff = oRequestOff.get();

                int countCheckin1Day = checkin1Day.size();

                if (requestOff != null) {
                    if (requestOff.getTimeOff().equals(TimeOff.FULLDAY)) {
                        content.append("<font color=cyan>OFF FULL DAY </font>");
                        countCheckin1Day = -1;
                    } else if (requestOff.getTimeOff().equals(TimeOff.MORNING))
                        content.append("<font color=cyan>OFF MORNING  </font><br>");
                    else if (requestOff.getTimeOff().equals(TimeOff.AFTERNOON))
                        content.append("<font color=cyan>OFF AFTERNOON  </font><br>");
                }

                if (countCheckin1Day > 0) {
                    content.append(checkin1Day.get(0) + "<br>");
                    if (countCheckin1Day > 1) {
                        content.append(checkin1Day.get(countCheckin1Day - 1));
                        if (checkin1Day.get(0).getResultTime() > 15)
                            countPen++;
                        if (checkin1Day.get(countCheckin1Day - 1).getResultTime() > 15)
                            countPen++;
                    } else if (countCheckin1Day == 1)
                        content.append("<font color=red>"
                                + " You forgot to checkout, penalty = 20.000 VND.</font>");
                    if (checkin1Day.get(0).getResultTime() > 15)
                        countPen++;
                    countPen++;
                } else if (countCheckin1Day == 0) {
                    content.append("<font color=red>"
                            + " You forgot to checkin and checkout => penalty = 40.000 VND.</font>");
                    countPen += 2;
                }
                content.append("<br>-----------------------------------------------<br>");
                workingDay = workingDayPlus1;
            }
            if (countPen != 0)
                content.append("<h2><b>Total checkin's penalties count: " + countPen + " = " + countPen * 20 + ".000 VND</b></h2>");

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(saturday.getTime());

            mailService.sendMail(em.getEmail(),
                    calendar.get(Calendar.WEEK_OF_YEAR) - 1 + "th Weekly Checkin list and penalties: "
                            + sdf.format(dateUtils.addDay(saturday, -5)).substring(0, 2) + " -> "
                            + sdf.format(saturday),
                    content.toString());
        }
    }

    @Scheduled(cron = "00 00 05 * * *")
    @Async
    public void applyNewWorkingHour() throws ParseException {
        Timestamp todayTS = new Timestamp(System.currentTimeMillis());
        Timestamp todayTSPlus1 = dateUtils.addDay(todayTS,1);

        Specification<RequestWorkingHourEntity> spec = new FilterSpecification<>(
                new SearchCriteria("applyDate", SearchCriteria.Operation.BETWEEN,
                        new SimpleDateFormat(DateFormat.y_Md).parse(todayTS.toString()),
                        new SimpleDateFormat(DateFormat.y_Md).parse(todayTSPlus1.toString())));

        spec = spec.and(new FilterSpecification<>(new SearchCriteria("status", SearchCriteria.Operation.EQUAL,Status.APPROVED)));

        List<RequestWorkingHourEntity> list = requestWorkingHourRepo.findAll(spec);

        if (!list.isEmpty()){
            for(RequestWorkingHourEntity entity:list){
                WorkingHourEntity workingHour = workingHourRepo.findByUserId(entity.getUser().getId());
                Long workingHourId = workingHour.getId();
                workingHour = mapper.map(entity, WorkingHourEntity.class);
                workingHour.setId(workingHourId);
                workingHourRepo.save(workingHour);

                StringBuilder content = new StringBuilder("<h1>Hi ");
                content.append(workingHour.getUser().getFullname());
                content.append("<br> Your new working hour has been applied <br>");
                content.append("<br> Remember to checkin/checkout with your new working hour</h1><br>");
                content.append(mapper.map(workingHour, WorkingHourDTO.class).toString());

                mailService.sendMail(workingHour.getUser().getEmail(),"Apply new Working hour "+new SimpleDateFormat(DateFormat.y_Md).format(todayTS), content.toString());
            }
        }
    }
}
