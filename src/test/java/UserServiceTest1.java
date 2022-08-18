import ducnh.springboot.model.entity.UserEntity;
import ducnh.springboot.repository.UserRepository;
import ducnh.springboot.service.IUserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ducnh.springboot.Application.class})
public class UserServiceTest1 {
    @Autowired
    UserRepository userRepository;
@Autowired
IUserService userService;

    List<UserEntity> list=new ArrayList<>();
   //@Before
    public void setUp() {
        list = LongStream.range(0,10).mapToObj(i->{
            UserEntity user = new UserEntity();
            user.setId(i);
            return user;
        }).collect(Collectors.toList());
        Mockito.when(userRepository.findAll()).thenReturn(list);
    }

    @Test
    public void test(){
        Assert.assertEquals(15,userService.findAll().size());
    }
}
