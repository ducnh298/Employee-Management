package ducnh.springboot.repository;

import ducnh.springboot.model.entity.UserEntity;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
@Autowired
    private TestEntityManager testEntityManager;

@Autowired
    private UserRepository userRepository;

    UserEntity found;
@Test
    public void findEmployee(){
    UserEntity user = new UserEntity();
    user.setFullname("Alex Hirch!");
    testEntityManager.persist(user);
    testEntityManager.flush();

    found = userRepository.findByFullname(UserEntity.class,user.getFullname());

    assertEquals(found.getFullname(),user.getFullname());
}

    @After
    public void clean() {
       testEntityManager.remove(found);
        testEntityManager.flush();
    }

    @org.junit.jupiter.api.Test
    void findAllForgetCheckin() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    assertEquals(userRepository.findAllForgetCheckin(new Timestamp(sdf.parse("2022-08-19").getTime()),new Timestamp(sdf.parse("2022-08-20").getTime())),11);
    }
}
