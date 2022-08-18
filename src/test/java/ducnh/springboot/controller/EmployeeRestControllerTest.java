package ducnh.springboot.controller;

import ducnh.springboot.Application;
import ducnh.springboot.dto.UserDTO;
import ducnh.springboot.service.IUserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ducnh.springboot.Application.class})
//@WebMvcTest(EmployeeRestController.class)
//@ContextConfiguration(classes= Application.class)
@AutoConfigureMockMvc
class EmployeeRestControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private IUserService userService;

    @Test
    void findById() throws Exception {
        UserDTO alex = new UserDTO();
        alex.setId(99L);

        given(userService.findById(99L)).willReturn(alex);

        mvc.perform(get("/employee-management/find")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}