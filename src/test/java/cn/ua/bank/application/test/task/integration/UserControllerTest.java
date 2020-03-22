package cn.ua.bank.application.test.task.integration;

import cn.ua.bank.application.test.task.model.OperationHistory;
import cn.ua.bank.application.test.task.repository.OperationHistoryRepository;
import cn.ua.bank.application.test.task.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest extends BaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OperationHistoryRepository operationHistoryRepository;

    @BeforeEach
    void login() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post("/bank-app-test-task/api/login")
                .content(mapToJson(testUser))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        token = result.getResponse().getContentAsString();
    }

    @AfterEach
    void deleteUser() {
        userRepository.deleteById(testUser.getEmail());
        operationHistoryRepository.deleteAllByUserEmail(testUser.getEmail());
    }

    @Test
    void testBalanceInNewUser_shouldBeZero() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/bank-app-test-task/api/user/balance")
                .header(AUTH_HTTP_HEADER, token)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("0.00"));
    }

    @Test
    void testDepositMoney_shouldAcceptedStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/bank-app-test-task/api/user/balance/deposit")
                .header(AUTH_HTTP_HEADER, token)
                .content("10.00")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(content().string(SUCCESS));
    }

    @Test
    void testDepositMoneyWithNegativeAmount_shouldNotAcceptableStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/bank-app-test-task/api/user/balance/deposit")
                .header(AUTH_HTTP_HEADER, token)
                .content("-10.00")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    void testWithdrawMoney_shouldAcceptedStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/bank-app-test-task/api/user/balance/deposit")
                .header(AUTH_HTTP_HEADER, token)
                .content("10.00")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        mockMvc.perform(MockMvcRequestBuilders
                .post("/bank-app-test-task/api/user/balance/withdraw")
                .header(AUTH_HTTP_HEADER, token)
                .content("5.00")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(content().string(SUCCESS));
    }

    @Test
    void testWithdrawMoney_shouldNotAcceptableStatusCauseNotEnoughMoney() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/bank-app-test-task/api/user/balance/withdraw")
                .header(AUTH_HTTP_HEADER, token)
                .content("10.00")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    void testWithdrawMoneyWithNegativeAmount_shouldNotAcceptableStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/bank-app-test-task/api/user/balance/withdraw")
                .header(AUTH_HTTP_HEADER, token)
                .content("-5.00")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    void testOperationHistory_shouldNotBeEmpty() throws Exception {
        depositMoneyApiCall("10.00");
        depositMoneyApiCall("10.00");
        depositMoneyApiCall("22.00");
        depositMoneyApiCall("8.00");
        depositMoneyApiCall("8.00");
        withdrawMoneyApiCall("8.00");
        withdrawMoneyApiCall("24.00");
        withdrawMoneyApiCall("1.00");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/bank-app-test-task/api/user/operation-histories")
                .header(AUTH_HTTP_HEADER, token)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        OperationHistory[] operationHistories = mapFromJson(content, OperationHistory[].class);
        assertEquals(operationHistories.length, 8);
        assertEquals(getUserBalance().compareTo(BigDecimal.valueOf(25.00)), 0);
    }

    private BigDecimal getUserBalance() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/bank-app-test-task/api/user/balance")
                .header(AUTH_HTTP_HEADER, token)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        return mapFromJson(result.getResponse().getContentAsString(), BigDecimal.class);
    }

    private void depositMoneyApiCall(String amount) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/bank-app-test-task/api/user/balance/deposit")
                .header(AUTH_HTTP_HEADER, token)
                .content(amount)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    private void withdrawMoneyApiCall(String amount) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/bank-app-test-task/api/user/balance/withdraw")
                .header(AUTH_HTTP_HEADER, token)
                .content(amount)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

}
