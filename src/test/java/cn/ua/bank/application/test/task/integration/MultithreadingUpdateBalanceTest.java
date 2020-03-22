package cn.ua.bank.application.test.task.integration;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class MultithreadingUpdateBalanceTest extends BaseControllerTest {

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

        depositMoneyApiCall("100.00");
    }

    @AfterEach
    void deleteUser() {
        operationHistoryRepository.deleteAllByUserEmail(testUser.getEmail());
        userRepository.deleteById(testUser.getEmail());
    }

    @Test
    void testMultithreadingUpdateBalance() throws Exception {
        Thread t1 = new Thread(() -> {
            try {
                for(int i = 0; i < 100; i++) {
                    depositMoneyApiCall("10.00");
                    System.out.println("Thread1 ===> Deposit number: " + i + " 10.00");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                for (int i = 0; i < 100; i++) {
                    withdrawMoneyApiCall("10.00");
                    System.out.println("Thread2 ===> Withdraw number: " + i + " 10.00");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        assertEquals(getUserBalance().compareTo(BigDecimal.valueOf(100)), 0);
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
