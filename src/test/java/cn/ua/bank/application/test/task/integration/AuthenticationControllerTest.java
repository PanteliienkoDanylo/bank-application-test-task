package cn.ua.bank.application.test.task.integration;

import cn.ua.bank.application.test.task.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AuthenticationControllerTest extends BaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testOk_shouldBeOkStatusAndStringOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/bank-app-test-task/api/test")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(OK));
    }

    @Test
    void testLogin_shouldBeOkStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/bank-app-test-task/api/login")
                .content(mapToJson(testUser))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testLoginWithIncorrectPassword_shouldBeUnauthorizedStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/bank-app-test-task/api/login")
                .content(mapToJson(new User(testUser.getEmail(), "incorrect password")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
