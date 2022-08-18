import ducnh.springboot.model.entity.UserEntity;
import ducnh.springboot.repository.UserRepository;
import ducnh.springboot.service.IUserService;
import ducnh.springboot.service.impl.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@RunWith(SpringRunner.class)
public class UserServiceTest2 {

    @TestConfiguration
    public static class UserServiceTest2Configuration {
        @Bean
        IUserService userService() {
            return new UserService();
        }
    }

    @Autowired
    private IUserService userService;

    @MockBean
    private UserRepository userRepository;

    List<UserEntity> list = new ArrayList<>();

    @Before
    public void setUp() {
        list = LongStream.range(0, 15).mapToObj(i -> {
            UserEntity user = new UserEntity();
            user.setId(i);
            return user;
        }).collect(Collectors.toList());
        Mockito.when(userRepository.findAll()).thenReturn(list);
    }

    @Test
    public void test() {
        Assert.assertEquals(15, userService.findAll().size());
    }
}
