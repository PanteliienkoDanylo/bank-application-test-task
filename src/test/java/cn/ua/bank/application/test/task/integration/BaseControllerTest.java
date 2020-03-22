package cn.ua.bank.application.test.task.integration;

import cn.ua.bank.application.test.task.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class BaseControllerTest {

    protected static final String SUCCESS = "success";
    protected static final String INVALID_PASSWORD = "invalid password";
    protected static final String AUTH_HTTP_HEADER = "auth-token";

    protected static User testUser = new User("qwerty@gmail.com", "qwerty");

    protected static String token;

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    protected <T> T mapFromJson(String json, Class<T> clazz) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }
}
