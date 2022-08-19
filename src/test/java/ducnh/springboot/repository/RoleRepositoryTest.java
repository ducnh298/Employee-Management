package ducnh.springboot.repository;

import ducnh.springboot.model.entity.RoleEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {
    @Mock
    RoleRepository mockRepository;

    @Test
    public void findAll(){
        List<RoleEntity> list = new ArrayList<>();
        for(int i=1;i<9;i++){
            RoleEntity role = new RoleEntity();
            role.setId(Long.parseLong(i+""));
            list.add(role);
        }
        Mockito.when(mockRepository.findAll()).thenReturn(list);

        int roleCount = mockRepository.findAll().size();

        Assert.assertEquals(8,roleCount);
    }
}