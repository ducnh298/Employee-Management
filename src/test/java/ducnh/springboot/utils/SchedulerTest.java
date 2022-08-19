package ducnh.springboot.utils;

import ducnh.springboot.service.IUserService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
class SchedulerTest {

    @Autowired
    IUserService userService;

    @Autowired
    Scheduler scheduler;

//    @Test
//    void checkinReminder() {
//        Assert.assertEquals(scheduler.checkinReminder(),);
//    }
}